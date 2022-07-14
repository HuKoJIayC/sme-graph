package com.github.hukojiayc.sme.graph.dto;

import java.util.ArrayList;
import java.util.List;

public enum OsbType {
  osb9042(TbType.vvb, "Головное отделение"),
  osb1111(TbType.vvb, "Головное отделение 1"),
  osb2222(TbType.vvb, "Головное отделение 2");

  private final TbType tb;
  private final String value;

  OsbType(TbType tb, String value) {
    this.tb = tb;
    this.value = value;
  }

  public TbType getTb() {
    return tb;
  }

  public String getValue() {
    return value;
  }

  public static List<OsbType> getOsbListByTb(TbType tb) {
    List<OsbType> osbTypeList = new ArrayList<>();
    for (OsbType osbType : OsbType.values()) {
      if (osbType.tb == tb) {
        osbTypeList.add(osbType);
      }
    }
    return osbTypeList;
  }
}
