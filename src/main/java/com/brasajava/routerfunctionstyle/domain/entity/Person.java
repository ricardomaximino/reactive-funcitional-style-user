package com.brasajava.routerfunctionstyle.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.Document;

import com.brasajava.routerfunctionstyle.message.model.Event;

@Document
public class Person extends AbstractAggregateRoot<Person> {

  @Id private String id;
  private String name;
  private String lastname;

  public Person() {}

  public Person(String id, String name, String lastname) {
    this.id = id;
    this.name = name;
    this.lastname = lastname;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Override
  public String toString() {
    return "LeadDTO [id=" + id + ", name=" + name + ", lastname=" + lastname + "]";
  }

  public Person create(Event event) {
    registerEvent(event);
    return this;
  }

  public Person update(Event event) {
    registerEvent(event);
    return this;
  }
}
