package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.dto.Components.CreateType;

public enum TbType {
  tb013("ЦЧБ", "Центрально-Черноземный банк"),
  tb016("УБ", "Уральский банк"),
  tb018("ББ", "Байкальский банк"),
  tb038("МБ", "Московский банк"),
  tb040("СРБ", "Среднерусский банк"),
  tb042("ВВБ", "Волго-Вятский банк"),
  tb044("СИБ", "Сибирский банк"),
  tb052("ЮЗБ", "Юго-Западный банк"),
  tb054("ПВБ", "Поволжский банк"),
  tb055("СЗБ", "Северо-Западный банк"),
  tb070("ДВБ", "Дальневосточный банк");

  private final String shortName;
  private final String fullName;

  TbType(String shortName, String fullName) {
    this.shortName = shortName;
    this.fullName = fullName;
  }

  public String getShortName() {
    return shortName;
  }

  public String getFullName() {
    return fullName;
  }

  public static String getOptions() {
    StringBuilder stringBuilder = new StringBuilder();
    for (TbType tbType : TbType.values()) {
      stringBuilder.append(
              CreateType.option.getValue(tbType.name(), tbType.getShortName())
          )
          .append("\n");
    }
    return stringBuilder.toString();
  }
}
