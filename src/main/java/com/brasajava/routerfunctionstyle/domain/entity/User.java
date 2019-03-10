package com.brasajava.routerfunctionstyle.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.Document;

import com.brasajava.routerfunctionstyle.message.model.Event;

@Document
public class User extends AbstractAggregateRoot<User> {

  @Id private String id;
  private String email;
  private String password;
  private Person person;

  public User(String id, String email, String password, Person person) {
    super();
    this.id = id;
    this.email = email;
    this.password = password;
    this.person = person;
  }

  public User() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return "User [id="
        + id
        + ", email="
        + email
        + ", password="
        + password
        + ", person="
        + person
        + "]";
  }

  public User create(Event event) {
    registerEvent(event);
    return this;
  }

  public User update(Event event) {
    registerEvent(event);
    return this;
  }
}
