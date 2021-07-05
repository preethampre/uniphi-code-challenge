package com.example.demo.pojos;

import lombok.Data;

@Data
public class RelationsBean {

  private double intersectionScore;
  private Integer cardinality;
  private String column;
  private String source;

}
