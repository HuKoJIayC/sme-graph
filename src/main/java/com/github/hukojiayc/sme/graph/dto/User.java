package com.github.hukojiayc.sme.graph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

  private Long id;
  private String token;
  private RoleType role;
  private String fullName;
}
