package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.Components;
import com.github.hukojiayc.sme.graph.dto.OsbType;
import com.github.hukojiayc.sme.graph.dto.TbType;
import com.github.hukojiayc.sme.graph.dto.VisitInfo;
import java.util.Date;
import java.util.List;

public class Application {

  public static void main(String[] args) {
    createTestData();
    Server.getInstance();
  }
  
  private static void createTestData() {
    Graph graph = Graph.getInstance();

    VisitInfo info = new VisitInfo();
    info.setDateStart(new Date());
    info.setDateEnd(new Date(16161661613L));
    info.setTb(TbType.tb042);
    info.setOsb(OsbType.osb0428589);
    info.setDirectors(List.of("Елена Ленина", "Мария Мариева"));
    info.setLeaders(List.of("Иван Иванов", Components.ViewType.leader.getValue("Пётр Петров")));
    
    graph.addToListInfoVisit(info);
  }
}
