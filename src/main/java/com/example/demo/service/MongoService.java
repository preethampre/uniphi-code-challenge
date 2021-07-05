package com.example.demo.service;

import com.example.demo.exceptions.DataException;
import com.example.demo.pojos.RelationsBean;
import java.util.List;

public interface MongoService {

  List<String> getColumnsHaveRelationsDefined() throws DataException;

  List<RelationsBean> getRelationsForAColumn(String columnName) throws DataException;
}
