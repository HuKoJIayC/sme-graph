package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.dto.Components.ViewType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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
public class Visit {

  private String id;
  private Date dateStart;
  private Date dateEnd;
  private TbType tb;
  private OsbType osb;
  private List<User> directors;
  private List<User> leaders;
  private List<User> leadersOnConfirmation;
  private User creator;

  public String getMonth() {
    String monthName =
        new SimpleDateFormat("MMMM", new Locale("ru")).format(dateStart);
    return String.valueOf(monthName.charAt(0)).toUpperCase() + monthName.substring(1);
  }

  public String getBetween() {
    return new SimpleDateFormat("dd.MM.yy").format(dateStart) + " - "
        + new SimpleDateFormat("dd.MM.yy").format(dateEnd);
  }

  public List<String> getFullNameDirectors() {
    return directors.stream().map(User::getFullName).collect(Collectors.toList());
  }

  public List<String> getFullNameLeaders() {
    return leaders.stream().map(User::getFullName).collect(Collectors.toList());
  }

  public List<String> getFullNameLeadersOnConfirmation() {
    return leadersOnConfirmation.stream()
        .map(user -> ViewType.leader.getValue(user.getFullName()))
        .collect(Collectors.toList());
  }
}
