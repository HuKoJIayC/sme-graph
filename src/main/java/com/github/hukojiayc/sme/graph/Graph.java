package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.SqlTables.Users;
import com.github.hukojiayc.sme.graph.dto.SqlTables.Visits;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.User;
import com.github.hukojiayc.sme.graph.dto.Visit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Graph {

  private static Graph graph = null;

  private final Database database;
  private final List<User> users;
  private final List<Visit> visits;

  private Graph() {
    log.info("Initialization graph");
    database = Database.getInstance();
    users = Collections.synchronizedList(new ArrayList<>());
    visits = Collections.synchronizedList(new ArrayList<>());
    readUsersFromDatabase();
    readVisitsFromDatabase();
  }

  public static synchronized Graph getInstance() {
    if (graph == null) {
      graph = new Graph();
    }
    return graph;
  }

  public List<User> getUsers() {
    return users;
  }

  public List<Visit> getVisits() {
    return visits;
  }

  public synchronized void addVisit(Visit visit) {
    log.info("Adding visit {} in database", visit.getId());
    database.execute(
        Visits.add.getSql(
            visit.getDateStart().getTime(),
            visit.getDateEnd().getTime(),
            visit.getTb().name(),
            visit.getOsb().name(),
            visit.getDirectors().stream()
                .map(user -> user.getId().toString()).collect(Collectors.joining(",")),
            visit.getLeaders().stream()
                .map(user -> user.getId().toString()).collect(Collectors.joining(",")),
            visit.getLeadersOnConfirmation().stream()
                .map(user -> user.getId().toString()).collect(Collectors.joining(",")),
            visit.getCreator().getId(),
            new Date().getTime()
        )
    );
    visits.add(visit);
  }

  private void readUsersFromDatabase() {
    List<Map<String, Object>> list = database.select(Users.get.getSql());
    for (Map<String, Object> map : list) {
      users.add(convertMapToUser(map));
    }
  }

  private void readVisitsFromDatabase() {
    log.info("Reading visits from database");
    List<Map<String, Object>> list = database.select(Visits.get.getSql());
    for (Map<String, Object> map : list) {
      visits.add(
          Visit.builder()
              .id(map.get("id").toString())
              .dateStart(new Date((long) map.get("date_start")))
              .dateEnd(new Date((long) map.get("date_end")))
              .tb(TbType.valueOf(map.get("tb").toString()))
              .osb(OsbType.valueOf(map.get("osb").toString()))
              .directors(getUsersFromStringDatabase(map.get("directors_id").toString()))
              .leaders(getUsersFromStringDatabase(map.get("leaders_id").toString()))
              .leadersOnConfirmation(
                  getUsersFromStringDatabase(map.get("leaders_id_on_confirmation").toString())
              )
              .creator(getUsersFromStringDatabase(map.get("creator_id").toString()).get(0))
              .build()
      );
    }
  }

  private List<User> getUsersFromStringDatabase(String usersId) {
    List<User> list = new ArrayList<>();
    if (usersId == null || usersId.length() == 0) {
      return list;
    }
    String[] ids = usersId.split(",");
    for (String id : ids) {
      List<Map<String, Object>> users = database.select(Users.getById.getSql(id));
      if (users.size() == 0) {
        continue;
      }
      list.add(convertMapToUser(users.get(0)));
    }
    return list;
  }

  private User convertMapToUser(Map<String, Object> map) {
    return User.builder()
        .id((long) map.get("telegram_id"))
        .role(RoleType.valueOf(map.get("role").toString()))
        .fullName(map.get("full_name").toString())
        .token(String.valueOf(map.get("token")))
        .build();
  }
}
