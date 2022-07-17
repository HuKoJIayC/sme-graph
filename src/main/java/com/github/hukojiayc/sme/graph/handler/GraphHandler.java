package com.github.hukojiayc.sme.graph.handler;

import static java.net.HttpURLConnection.HTTP_OK;

import com.github.hukojiayc.sme.graph.Graph;
import com.github.hukojiayc.sme.graph.dto.Components;
import com.github.hukojiayc.sme.graph.dto.Components.CreateType;
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
import java.util.UUID;
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
    // todo Токен задаётся при создании меню с переходом в график и добавляется в базу
    if (exchange.getRequestURI().getPath().contains(path + "/login/")) {
      // set token and redirect
      exchange.getResponseHeaders().put(
          "Set-Cookie",
          List.of("token=" + exchange.getRequestURI().getPath().substring(13) + ";")
      );
      exchange.getResponseHeaders().add("Location", path);
      exchange.sendResponseHeaders(301, 0);
      exchange.close();
      return;
    }
    // todo check token from cookies in db
    User user5 = User.builder()
        .id(5L)
        .fullName("Иван Пупкин")
        .role(RoleType.creator)
        .build();
    // todo else unauthorized

    String responseText;
    if (exchange.getRequestURI().getPath().indexOf(path + "/join") == 0) {
      // todo logic
//      graph.getVisitList().stream().filter(visit ->)
      exchange.getResponseHeaders().add("Location", path);
      exchange.sendResponseHeaders(301, 0);
      exchange.close();
      return;
    } else if (exchange.getRequestURI().getPath().indexOf(path + "/create") == 0) {
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
    } else {

      responseText = createHtml(graph.getVisitList(), user5);
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
    Visit visit = Visit.builder()
        .id(UUID.randomUUID().toString())
        .dateStart(simpleDateFormat.parse(data.get("dateStart")))
        .dateEnd(simpleDateFormat.parse(data.get("dateEnd")))
        .tb(TbType.valueOf(data.get("tb")))
        .osb(OsbType.valueOf(data.get("osb")))
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
        .build();
    // adding in list
    graph.addToListVisit(visit);
  }

  private String createHtml(List<Visit> visitList, User user) {
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
          Components.ViewType.widget.getValue(
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
      widgets.append(Components.ViewType.add.getValue(path));
    }
    return Components.ViewType.index.getValue(
        user.getId(),
        user.getRole() == RoleType.creator ? "director" : "leader",
        widgets.toString()
    );
  }

  @Override
  public String getPath() {
    return path;
  }
}
