package com.example.demo.service.impl;

import com.example.demo.commons.GeneralConstants;
import com.example.demo.exceptions.DataException;
import com.example.demo.pojos.RelationsBean;
import com.example.demo.service.MongoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoServiceImpl implements MongoService {

  @Autowired private MongoClient mongoClient;

  @Override
  @SuppressWarnings("unchecked")
  public List<String> getColumnsHaveRelationsDefined() throws DataException {
    try {

      var mongoDatabase = mongoClient.getDatabase(GeneralConstants.DATABASE_NAME);
      MongoCollection<Document> collection =
          mongoDatabase.getCollection(GeneralConstants.FINANCIAL_COLLECTION);

      AggregateIterable<Document> aggregateResult =
          collection.aggregate(
              Arrays.asList(
                  new Document("$limit", 1),
                  new Document("$project", new Document("_id", 1)),
                  createLookUpDocument(GeneralConstants.FINANCIAL_COLLECTION),
                  createLookUpDocument(GeneralConstants.IMDB_COLLECTION),
                  createLookUpDocument(GeneralConstants.PUBS_COLLECTION)));

      List<String> qualifiedNames = new ArrayList<>();
      for (Document row : aggregateResult) {

        qualifiedNames.addAll(
            loopCollectionData(
                (List<Document>) row.get("financial"), GeneralConstants.FINANCIAL_COLLECTION));
        qualifiedNames.addAll(
            loopCollectionData((List<Document>) row.get("imdb"), GeneralConstants.IMDB_COLLECTION));
        qualifiedNames.addAll(
            loopCollectionData((List<Document>) row.get("pubs"), GeneralConstants.PUBS_COLLECTION));
      }

      return qualifiedNames;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new DataException(
          GeneralConstants.ERROR,
          GeneralConstants.SOMETHING_WENT_WRONG,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private List<String> loopCollectionData(List<Document> documents, String collectionName) {
    List<String> columnsList = new ArrayList<>();
    for (Document document : documents) {
      columnsList.add(
          String.join(
              ".",
              Arrays.asList(
                  GeneralConstants.DATABASE_NAME,
                  collectionName,
                  document.getString("_id"),
                  document.get("columnsInfo", Document.class).getString("columnName"))));
    }

    return columnsList;
  }

  private Document createLookUpDocument(String collectionName) {
    var matchDocument =
        new Document(
            "$match",
            new Document(
                "columnsInfo.relations",
                new Document("$exists", true)
                    .append("$type", "array")
                    .append("$not", new Document("$size", 0))));
    var unwindD = new Document("$unwind", "$columnsInfo");
    var projectDocument = new Document("$project", new Document("columnsInfo.columnName", 1));

    return new Document(
        "$lookup",
        new Document("from", collectionName)
            .append("as", collectionName)
            .append(
                "pipeline", Arrays.asList(matchDocument, unwindD, matchDocument, projectDocument)));
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<RelationsBean> getRelationsForAColumn(String columnName) throws DataException {
    try {
      if (columnName == null || columnName.equals("")) {
        throw new DataException(
            GeneralConstants.BAD_REQUEST,
            "Column Name can not be empty or null",
            HttpStatus.BAD_REQUEST);
      }

      var mongoDatabase = mongoClient.getDatabase(GeneralConstants.DATABASE_NAME);
      MongoCollection<Document> collection =
          mongoDatabase.getCollection(GeneralConstants.FINANCIAL_COLLECTION);

      AggregateIterable<Document> aggregateResult =
          collection.aggregate(
              Arrays.asList(
                  new Document("$limit", 1),
                  new Document("$project", new Document("_id", 1)),
                  createLookUpDocumentForGetRelations(
                      GeneralConstants.FINANCIAL_COLLECTION, columnName),
                  createLookUpDocumentForGetRelations(GeneralConstants.IMDB_COLLECTION, columnName),
                  createLookUpDocumentForGetRelations(GeneralConstants.PUBS_COLLECTION, columnName),
                  new Document(
                      "$project",
                      new Document(
                          "union",
                          new Document(
                              "$concatArrays",
                              Arrays.asList(
                                  ("$" + GeneralConstants.FINANCIAL_COLLECTION),
                                  ("$" + GeneralConstants.IMDB_COLLECTION),
                                  ("$" + GeneralConstants.PUBS_COLLECTION))))),
                  new Document("$unwind","$union")));


      List<RelationsBean> relationsBeans = new ArrayList<>();
      for (Document row : aggregateResult) {
        List<Document> documents= (List<Document>) row.get("union", Document.class)
            .get("columnsInfo", Document.class).get("relations");

        relationsBeans.addAll(mapRelationDocumentToBean(documents));

      }

      return relationsBeans;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new DataException(
          GeneralConstants.ERROR,
          GeneralConstants.SOMETHING_WENT_WRONG,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private List<RelationsBean> mapRelationDocumentToBean(List<Document> documents)
  {
    List<RelationsBean> relationsBeanList=new ArrayList<>();
    for(Document document:documents)
    {
      var relationsBean=new RelationsBean();
      relationsBean.setCardinality(document.getInteger("cardinality"));
      relationsBean.setIntersectionScore(document.getDouble("intersectionScore"));
      relationsBean.setColumn(document.getString("column"));
      relationsBean.setSource(document.getString("source"));

      relationsBeanList.add(relationsBean);
    }

    return relationsBeanList;
  }


  private Document createLookUpDocumentForGetRelations(String collectionName, String columnName) {
    var matchDocument =
        new Document(
            "$match",
            new Document(
                "columnsInfo.relations",
                new Document("$exists", true)
                    .append("$type", "array")
                    .append("$not", new Document("$size", 0))));

    var unwindD = new Document("$unwind", "$columnsInfo");

    var matchColumn =
        new Document(
            "$match", new Document("columnsInfo.columnName", new Document("$eq", columnName)));

    return new Document(
        "$lookup",
        new Document("from", collectionName)
            .append("as", collectionName)
            .append("pipeline", Arrays.asList(matchDocument, unwindD, matchDocument, matchColumn)));
  }
}
