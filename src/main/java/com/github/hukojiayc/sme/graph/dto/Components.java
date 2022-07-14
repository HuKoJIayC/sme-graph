package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.utils.ApplicationUtils;
import com.github.hukojiayc.sme.graph.utils.ResourceUtils;

public class Components {

  public enum ViewType {
    index(ResourceUtils.getFileAsString("components/view/index.html").orElseThrow()),
    widget(ResourceUtils.getFileAsString("components/view/widget.html").orElseThrow()),
    leader(ResourceUtils.getFileAsString("components/view/leader.html").orElseThrow()),
    add(ResourceUtils.getFileAsString("components/view/add.html").orElseThrow());

    private final String value;

    ViewType(String value) {
      this.value = value;
    }

    public String getValue(Object... args) {
      return ApplicationUtils.addArgsToValue(value, args);
    }
  }

  public enum CreateType {
    index(ResourceUtils.getFileAsString("components/create/index.html").orElseThrow());

    private final String value;

    CreateType(String value) {
      this.value = value;
    }

    public String getValue(Object... args) {
      return ApplicationUtils.addArgsToValue(value, args);
    }
  }


}
