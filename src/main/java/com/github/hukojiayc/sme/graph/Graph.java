package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.VisitInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {

  private static Graph graph = null;

  private final List<VisitInfo> visitInfoList = Collections.synchronizedList(new ArrayList<>());

  private Graph() {
    // todo read db
  }

  public static synchronized Graph getInstance() {
    if (graph == null) {
      graph = new Graph();
    }
    return graph;
  }

  public List<VisitInfo> getVisitInfoList() {
    return visitInfoList;
  }

  public void addToListInfoVisit(VisitInfo visitInfo) {
    visitInfoList.add(visitInfo);
  }
}
