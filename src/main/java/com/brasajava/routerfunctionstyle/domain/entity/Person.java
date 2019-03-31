package com.brasajava.routerfunctionstyle.domain.entity;

import java.time.LocalDate;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.brasajava.routerfunctionstyle.message.model.Event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Person extends AbstractAggregateRoot<Person> {

  private String name;
  private String lastname;
  private LocalDate birthday;

  public Person create(Event event) {
    registerEvent(event);
    return this;
  }

  public Person update(Event event) {
    registerEvent(event);
    return this;
  }
}
