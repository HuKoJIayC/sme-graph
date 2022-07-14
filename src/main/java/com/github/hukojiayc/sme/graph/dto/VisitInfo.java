package com.github.hukojiayc.sme.graph.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
