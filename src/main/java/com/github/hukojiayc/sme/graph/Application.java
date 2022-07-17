package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.DatabaseSqlType;
import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.Visit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Application {

  public static void main(String[] args) {
    createTestData();
    Database.getInstance();
    Server.getInstance();
  }
  
  private static void createTestData() {
    Database database = Database.getInstance();
    // adding users
    database.execute(
        DatabaseSqlType.usersAdd.getSql(
            1,
            RoleType.creator.name(),
            "Елена Ленина",
            1
        ),
        DatabaseSqlType.usersAdd.getSql(
            2,
            RoleType.creator.name(),
            "Мария Мариева",
            1
        ),
        DatabaseSqlType.usersAdd.getSql(
            3,
            RoleType.viewer.name(),
            "Иван Иванов",
            1
        ),
        DatabaseSqlType.usersAdd.getSql(
            4,
            RoleType.viewer.name(),
            "Пётр Петров",
            1
        )
    );
    // adding visits if not
    Graph graph = Graph.getInstance();
    if (graph.getVisits().size() == 0) {
      Visit visit = new Visit();
      visit.setId(UUID.randomUUID().toString());
      visit.setDateStart(new Date());
      visit.setDateEnd(new Date(16161661613L));
      visit.setTb(TbType.tb042);
      visit.setOsb(OsbType.osb0428589);
      visit.setDirectors(List.of(graph.getUsers().get(0), graph.getUsers().get(1)));
      visit.setLeaders(List.of(graph.getUsers().get(2)));
      visit.setLeadersOnConfirmation(List.of(graph.getUsers().get(3)));
      visit.setCreator(graph.getUsers().get(0));
      graph.addVisit(visit);
    }
  }
}
