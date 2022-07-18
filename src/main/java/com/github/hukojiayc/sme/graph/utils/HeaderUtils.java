//package com.github.hukojiayc.sme.graph.utils;
//
//import com.github.hukojiayc.sme.graph.Database;
//import com.github.hukojiayc.sme.graph.Graph;
//import com.github.hukojiayc.sme.graph.dto.SqlTables.Users;
//import com.github.hukojiayc.sme.graph.dto.User;
//import com.sun.net.httpserver.Headers;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//public class HeaderUtils {
//
//  public static Optional<User> getUserByHeaders(Headers headers) {
//    Optional<String> token = getTokenFromCookies(headers.get("Cookie"));
//    if (token.isEmpty()) {
//      return Optional.empty();
//    }
//    Graph graph = Graph.getInstance();
//    // check local
//    List<User> users = graph.getUsers().stream()
//        .filter(user -> user.getToken().equals(token.get())).collect(Collectors.toList());
//    if (users.size() > 0) {
//      return Optional.of(users.get(0));
//    }
//    // check in db
//    List<Map<String, Object>> list = Database.getInstance().select(
//        Users.getByToken.getSql(token.get())
//    );
//    if (list.size() == 0) {
//      return Optional.empty();
//    }
//    User user = graph.convertMapToUser(list.get(0));
//    graph.updateToken(user.getId(), user.getToken());
//    return Optional.of(user);
//  }
//
//  public static Optional<String> getTokenFromCookies(List<String> cookies) {
//    if (cookies == null || cookies.size() == 0) {
//      return Optional.empty();
//    }
//    for (String cookie : cookies) {
//      int index = cookie.indexOf("TOKEN=");
//      if (index > -1) {
//        return Optional.of(cookie.substring(index + 6));
//      }
//    }
//    return Optional.empty();
//  }
//}
