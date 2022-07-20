package com.github.hukojiayc.sme.graph.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;

public class LoginHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"GET".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, 0);
      exchange.close();
    }
    exchange.getResponseHeaders().put(
        "Set-Cookie",
        List.of("TOKEN=" + exchange.getRequestURI().getPath().substring(7) + ";")
    );
    exchange.getResponseHeaders().add("Location", "http://localhost:5050/graph");
    exchange.sendResponseHeaders(301, 0);
    exchange.close();
  }
}
