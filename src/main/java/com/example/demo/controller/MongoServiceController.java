package com.example.demo.controller;

import com.example.demo.controller.resource.AbstractController;
import com.example.demo.exceptions.DataException;
import com.example.demo.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/mongo")
public class MongoServiceController extends AbstractController {

  @Autowired
  private MongoService mongoService;

  @GetMapping("/columns")
  public ResponseEntity getColumnsHaveRelations() throws DataException
  {
    try
    {
      return buildResponse(mongoService.getColumnsHaveRelationsDefined());
    }
    catch (DataException e)
    {
      return buildError(e);
    }
  }

  @GetMapping("/relations")
  public ResponseEntity getRelationsOfColumnName(@RequestParam String columnName) throws DataException
  {
    try
    {
      return buildResponse(mongoService.getRelationsForAColumn(columnName));
    }
    catch (DataException e)
    {
      return buildError(e);
    }
  }

}
