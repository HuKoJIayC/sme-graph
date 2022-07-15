package com.github.hukojiayc.sme.graph.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import com.github.hukojiayc.sme.graph.Graph;
import com.github.hukojiayc.sme.graph.dto.Components;
import com.github.hukojiayc.sme.graph.dto.Components.CreateType;
import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.VisitInfo;
import com.github.hukojiayc.sme.graph.utils.ServerUtils;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class GraphHandler extends BaseHttp {

  private final String path = "/graph";
  private final Graph graph = Graph.getInstance();

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
        requestBody.ifPresent(this::addVisitInfoFromForm);
        exchange.getResponseHeaders().add("Location", path);
        exchange.sendResponseHeaders(301, 0);
        exchange.close();
        return;
      }
      responseText = CreateType.index.getValue(
          TbType.getOptions(),
          OsbType.getOptions(),
          "Иван Пупкин", // todo
          "",
          path
      );
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
      exchange.getResponseHeaders().add("Location", path);
      exchange.sendResponseHeaders(301, 0);
      exchange.close();
      return;
    } else {

      // todo check token
      RoleType role = RoleType.creator;

      responseText = createHtml(graph.getVisitInfoList(), role);
    }

    exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(responseText);
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    exchange.sendResponseHeaders(HTTP_OK, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();
  }

  @SneakyThrows
  private void addVisitInfoFromForm(String info) {
    System.out.println();
    String[] args = info.split("&");
    // adding in map
    Map<String, String> data = new HashMap<>();
    for (String arg : args) {
      int index = arg.indexOf("=");
      if (index < 0) {
        continue;
      }
      String key = arg.substring(0, index);
      String value = arg.substring(index + 1);
      data.put(key, value);
    }
    // creating object
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    VisitInfo visitInfo = VisitInfo.builder()
        .dateStart(simpleDateFormat.parse(data.get("dateStart")))
        .dateEnd(simpleDateFormat.parse(data.get("dateEnd")))
        .tb(TbType.valueOf(data.get("tb")))
        .osb(OsbType.valueOf(data.get("osb")))
        .directors(
            Arrays.asList(
                URLDecoder.decode(data.get("directors"), Charset.defaultCharset()).split(",")
            )
        )
        .leaders(
            Arrays.asList(
                URLDecoder.decode(data.get("leaders"), Charset.defaultCharset()).split(",")
            )
        )
        .build();
    // adding in list
    graph.addToListInfoVisit(visitInfo);
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
              visitInfo.getTb().getShortName(),
              visitInfo.getOsb().getCity(),
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
