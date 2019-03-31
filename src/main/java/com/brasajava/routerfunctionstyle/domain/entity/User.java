package com.brasajava.routerfunctionstyle.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.Document;

import com.brasajava.routerfunctionstyle.message.model.Event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractAggregateRoot<User> {

  @Id private String id;
  private String username;
  private String email;
  private String password;
  private Person person;

  public User create(Event event) {
    registerEvent(event);
    return this;
  }

  public User update(Event event) {
    registerEvent(event);
    return this;
  }
}
