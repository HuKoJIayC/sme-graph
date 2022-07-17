package com.github.hukojiayc.sme.graph.dto;

public enum DatabaseColumnType {
  integer(4),
  string(12),
  capacity(7);

  private final int value;

  DatabaseColumnType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
