package com.github.hukojiayc.sme.graph.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import com.github.hukojiayc.sme.graph.dto.Components;
import com.github.hukojiayc.sme.graph.dto.Components.CreateType;
import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.VisitInfo;
import com.github.hukojiayc.sme.graph.utils.ServerUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class GraphHandler extends BaseHttp {

  private final String path = "/graph";

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    log.info(
        "Incoming request method {} by URL {}",
        exchange.getRequestMethod(),
        exchange.getRequestURI().getPath()
    );
    String responseText;
    if (exchange.getRequestURI().getPath().indexOf(path + "/create") == 0) {
      if ("POST".equals(exchange.getRequestMethod())) {
        Optional<String> requestBody = ServerUtils.getRequestBody(exchange.getRequestBody());
        if (requestBody.isPresent()) {
          // todo adding visit
        }
        exchange.getResponseHeaders().add("Location", "http://localhost:5050" + path);
        exchange.sendResponseHeaders(301, 0);
        exchange.close();
        return;
      }
      responseText = CreateType.index.getValue(path);
    } else if (!"GET".equals(exchange.getRequestMethod())) {
      log.warn("Method {} not allowed", exchange.getRequestMethod());
      exchange.sendResponseHeaders(HttpStatus.SC_METHOD_NOT_ALLOWED, 0);
      exchange.close();
      return;
    } else if (!exchange.getRequestURI().getPath().equals(path)) {
      // set token and redirect
      exchange.getResponseHeaders().put(
          "Set-Cookie",
          List.of("ID=" + exchange.getRequestURI().getPath().substring(7) + ";")
      );
      exchange.getResponseHeaders().add("Location", "http://localhost:5050" + path);
      exchange.sendResponseHeaders(301, 0);
      exchange.close();
      return;
    } else {

      // todo check token
      RoleType role = RoleType.creator;

      // todo test
      VisitInfo info = new VisitInfo();
      info.setDateStart(new Date());
      info.setDateEnd(new Date(16161661613L));
      info.setTb(TbType.vvb);
      info.setOsb(OsbType.osb9042);
      info.setDirectors(List.of("Елена Ленина", "Мария Мариева"));
//      info.setLeaders(List.of("Иван Иванов"));
      info.setLeaders(List.of("Иван Иванов", Components.ViewType.leader.getValue("Пётр Петров")));
      List<VisitInfo> list = new ArrayList<>();
      list.add(info);
      list.add(info);
      list.add(info);
      list.add(info);
      list.add(info);
      list.add(info);
      list.add(info);
      list.add(info);

      responseText = createHtml(list, role);
    }

    exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(responseText);
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    exchange.sendResponseHeaders(HTTP_OK, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();
  }

  private String createHtml(
      List<VisitInfo> visitInfoList,
      RoleType roleType
  ) {
    StringBuilder widgets = new StringBuilder();
    for (VisitInfo visitInfo : visitInfoList) {
      widgets.append(
          Components.ViewType.widget.getValue(
              visitInfo.getMonth(),
              visitInfo.getBetween(),
              visitInfo.getTb().getValue(),
              visitInfo.getOsb().getValue(),
              String.join(", ", visitInfo.getDirectors()),
              String.join(", ", visitInfo.getLeaders())
          )
      );
    }
    if (roleType == RoleType.creator) {
      widgets.append(Components.ViewType.add.getValue(path));
    }
    return Components.ViewType.index.getValue(widgets.toString());
  }

  @Override
  public String getPath() {
    return path;
  }
}
