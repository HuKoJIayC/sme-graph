package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.ApplicationUtils;

public enum ComponentType {
  html(""
      + "<!DOCTYPE html>\n"
      + "{}{}"
      + "</html>\n"),
  head(""
      + "<head>\n"
      + "    <meta charset=\"utf-8\">\n"
      + "    <meta name=\"viewport\" content=\"width=device-width\">\n"
      + "    <title>График полевых визитов</title>\n"
      + "    <style>\n"
      + "        .container {\n"
      + "            display: grid;\n"
      + "            grid-gap: 5px;\n"
      + "            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));\n"
      + "        }\n"
      + "\n"
      + "        .widget {\n"
      + "            height: 230px;\n"
      + "            background: #e3ffe1;\n"
      + "            padding: 15px;\n"
      + "            margin: 15px;\n"
      + "            box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);\n"
      + "            border-radius: 10px;\n"
      + "        }\n"
      + "\n"
      + "        a {\n"
      + "            text-decoration: none;\n"
      + "            color: black;\n"
      + "        }\n"
      + "    </style>\n"
      + "</head>\n"),
  body(""
      + "<body>\n"
      + "    {}\n"
      + "</body>\n"),
  widget("<div class=\"widget\">{}</div>\n"),
  container("<div class=\"container\">{}</div>\n");

  private final String value;

  ComponentType(String value) {
    this.value = value;
  }

  public String getValue(Object... args) {
    return ApplicationUtils.addArgsToValue(value, args);
  }
}
