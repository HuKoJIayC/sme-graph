package com.github.hukojiayc.sme.graph.handler;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.github.hukojiayc.sme.graph.Graph;
import com.github.hukojiayc.sme.graph.dto.Components.CreateType;
import com.github.hukojiayc.sme.graph.dto.Components.ViewType;
import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.User;
import com.github.hukojiayc.sme.graph.dto.Visit;
import com.github.hukojiayc.sme.graph.utils.ServerUtils;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
    exchange.getResponseHeaders().put(
        "Cache-Control",
        List.of("no-cache", "no-store", "must-revalidate")
    );
    String uri = exchange.getRequestURI().getPath();
    log.info("Incoming request method {} by URL {}", exchange.getRequestMethod(), uri);
    if (!uri.contains(path + "/")) {
      exchange.sendResponseHeaders(HTTP_UNAUTHORIZED, 0);
      exchange.close();
      return;
    }
    String token = uri.substring((path + "/").length());
    if (token.contains("/")) {
      token = token.substring(0, token.indexOf("/"));
    }
    // todo Токен задаётся при создании меню с переходом в график и добавляется в базу
    Optional<User> user = graph.getUserByToken(token);
    if (user.isEmpty()) {
      exchange.sendResponseHeaders(403, 0);
      exchange.close();
    } else if (uri.indexOf(path + "/" + token + "/join/") == 0) {
      long id = Long.parseLong(uri.substring((path + "/" + token + "/join/").length()));
      graph.updateVisit(id, user.get());
      exchange.getResponseHeaders().add("Location", path + "/" + token);
      exchange.sendResponseHeaders(301, 0);
      exchange.close();
    } else if (uri.indexOf(path + "/" + token + "/create") == 0) {
      if ("POST".equals(exchange.getRequestMethod())) {
        Optional<String> requestBody = ServerUtils.getRequestBody(exchange.getRequestBody());
        requestBody.ifPresent(this::addVisitInfoFromForm);
        exchange.getResponseHeaders().add("Location", path + "/" + token);
        exchange.sendResponseHeaders(301, 0);
        exchange.close();
      } else {
        sendResponse(exchange, createChangeHtml(user.get()));
      }
    } else if (!"GET".equals(exchange.getRequestMethod())) {
      log.warn("Method {} not allowed", exchange.getRequestMethod());
      exchange.sendResponseHeaders(HttpStatus.SC_METHOD_NOT_ALLOWED, 0);
      exchange.close();
    } else {
      sendResponse(exchange, createViewHtml(graph.getVisits(), user.get()));
    }
  }

  @SneakyThrows
  private void sendResponse(HttpExchange exchange, String html) {
    exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(html);
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
//    Visit visit = Visit.builder()
//        .id(UUID.randomUUID().toString())
//        .dateStart(simpleDateFormat.parse(data.get("dateStart")))
//        .dateEnd(simpleDateFormat.parse(data.get("dateEnd")))
//        .tb(TbType.valueOf(data.get("tb")))
//        .osb(OsbType.valueOf(data.get("osb")))
// todo
//        .directors(
//            Arrays.asList(
//                URLDecoder.decode(data.get("directors"), Charset.defaultCharset()).split(",")
//            )
//        )
//        .leaders(
//            Arrays.asList(
//                URLDecoder.decode(data.get("leaders"), Charset.defaultCharset()).split(",")
//            )
//        )
//        .build();
    // adding in list
//    graph.addVisit(visit);
  }

  private String createViewHtml(List<Visit> visitList, User user) {
    StringBuilder widgets = new StringBuilder();
    for (Visit visit : visitList) {
      StringBuilder leaders = new StringBuilder(
          String.join(", ", visit.getFullNameLeaders())
      );
      if (visit.getLeadersOnConfirmation() != null
          && visit.getLeadersOnConfirmation().size() > 0) {
        if (leaders.length() > 0) {
          leaders.append(", ");
        }
        leaders.append(
            String.join(", ", visit.getFullNameLeadersOnConfirmation())
        );
      }
      widgets.append(
          ViewType.widget.getValue(
              visit.getId(),
              visit.getMonth(),
              visit.getBetween(),
              visit.getTb().getShortName(),
              visit.getOsb().getCity(),
              String.join(", ", visit.getFullNameDirectors()),
              leaders.toString()
          )
      );
    }
    if (user.getRole() == RoleType.creator) {
      widgets.append(
          ViewType.add.getValue(path + "/" + user.getToken() + "/create")
      );
    }
    return ViewType.index.getValue(
        user.getRole() == RoleType.creator ? "director" : "leader",
        user.getFullName(),
        user.getToken(),
        widgets.toString()
    );
  }

  private String createChangeHtml(User user) {
    StringBuilder directors = new StringBuilder();
    StringBuilder leaders = new StringBuilder();
    for (User user1 : graph.getUsers()) {
      String checkbox = CreateType.checkbox.getValue(
          user1.getId(),
          user1.getId().equals(user.getId()) ? "checked" : "",
          user1.getFullName()
      );
      if (user1.getRole() == RoleType.creator) {
        directors.append(checkbox);
      } else {
        leaders.append(checkbox);
      }
    }
    return CreateType.index.getValue(
        TbType.getOptions(),
        OsbType.getOptions(),
        directors,
        leaders,
        path + "/" + user.getToken()
    );
  }

  @Override
  public String getPath() {
    return path;
  }
}
