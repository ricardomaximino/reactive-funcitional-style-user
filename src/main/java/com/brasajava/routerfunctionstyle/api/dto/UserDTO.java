package com.brasajava.routerfunctionstyle.api.dto;

import com.brasajava.routerfunctionstyle.domain.entity.Person;

import lombok.Data;

@Data
public class UserDTO {

  private String id;
  private String email;
  private String password;
  private Person person;
}
