package com.github.hukojiayc.sme.graph.dto;

public enum TbType {
  vvb("ВВБ");

  private final String value;

  TbType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
