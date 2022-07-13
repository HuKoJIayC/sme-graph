package com.github.hukojiayc.sme.graph.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import com.github.hukojiayc.sme.graph.dto.ComponentType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpStatus;

public class GraphHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!"GET".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(HttpStatus.SC_METHOD_NOT_ALLOWED, 0);
      exchange.close();
    }

    String responseText = createTestHtml();
//    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
//    exchange.getResponseHeaders().set("Cache-Control", "no-cache");
//    exchange.sendResponseHeaders(HttpStatus.SC_OK, responseText.length());
//    exchange.getResponseBody().write(responseText.getBytes(StandardCharsets.UTF_8));
//    exchange.close();

    exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
    ByteBuffer buffer = Charset.forName("UTF-8").encode(responseText);
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    exchange.sendResponseHeaders(HTTP_OK, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();

//    try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
//      try (ByteArrayInputStream bis = new ByteArrayInputStream(responseText.getBytes())) {
//        byte[] buffer = new byte[512]; // todo get BUFFER_SIZE
//        int count;
//        while ((count = bis.read(buffer)) != -1) {
//          out.write(buffer, 0, count);
//        }
//        out.close();
//      }
//    }
  }

  private String createTestHtml() {
    StringBuilder widgets = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      widgets.append(ComponentType.widget.getValue(i));
    }
    return ComponentType.html.getValue(
        ComponentType.head.getValue(),
        ComponentType.body.getValue(
            ComponentType.container.getValue(widgets.toString())
        )
    );
  }
}
