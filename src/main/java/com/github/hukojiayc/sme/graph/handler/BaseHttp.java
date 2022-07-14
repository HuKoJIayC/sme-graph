package com.github.hukojiayc.sme.graph.handler;

import com.sun.net.httpserver.HttpHandler;

public abstract class BaseHttp implements HttpHandler {

  public abstract String getPath();

  public BaseHttp reInit(Object object) {
    return this;
  }
}
