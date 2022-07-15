package com.github.hukojiayc.sme.graph.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitInfo {

  private Date dateStart;
  private Date dateEnd;
  private TbType tb;
  private OsbType osb;
  private List<String> directors;
  private List<String> leaders;

  public String getMonth() {
    String monthName =
        new SimpleDateFormat("MMMM", new Locale("ru")).format(dateStart);
    return String.valueOf(monthName.charAt(0)).toUpperCase() + monthName.substring(1);
  }

  public String getBetween() {
    return new SimpleDateFormat("dd.MM.yy").format(dateStart) + " - "
        + new SimpleDateFormat("dd.MM.yy").format(dateEnd);
  }

  public void setDirectors(List<String> directors) {
    if (directors == null) {
      return;
    } else if (this.directors == null) {
      this.directors = new ArrayList<>();
    }
    for (int i = 0; i < directors.size(); i++) {
      this.directors.add(directors.get(i).trim());
    }
  }

  public void setLeaders(List<String> leaders) {
    if (leaders == null) {
      return;
    } else if (this.leaders == null) {
      this.leaders = new ArrayList<>();
    }
    for (int i = 0; i < leaders.size(); i++) {
      this.leaders.add(i, leaders.get(i).trim());
    }
  }
}
