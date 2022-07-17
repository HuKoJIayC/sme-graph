package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.RoleType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.User;
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
    Graph graph = Graph.getInstance();

    User user1 = User.builder()
        .id(1L)
        .fullName("Елена Ленина")
        .role(RoleType.creator)
        .build();
    User user2 = User.builder()
        .id(2L)
        .fullName("Мария Мариева")
        .role(RoleType.creator)
        .build();
    User user3 = User.builder()
        .id(3L)
        .fullName("Иван Иванов")
        .role(RoleType.creator)
        .build();
    User user4 = User.builder()
        .id(4L)
        .fullName("Пётр Петров")
        .role(RoleType.creator)
        .build();

    Visit visit = new Visit();
    visit.setId(UUID.randomUUID().toString());
    visit.setDateStart(new Date());
    visit.setDateEnd(new Date(16161661613L));
    visit.setTb(TbType.tb042);
    visit.setOsb(OsbType.osb0428589);
    visit.setDirectors(List.of(user1, user2));
    visit.setLeaders(List.of(user3));
    visit.setLeadersOnConfirmation(List.of(user4));

    graph.addToListVisit(visit);
  }
}
