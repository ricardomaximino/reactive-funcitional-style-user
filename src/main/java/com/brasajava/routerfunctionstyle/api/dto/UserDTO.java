package com.brasajava.routerfunctionstyle.api.dto;

import com.brasajava.routerfunctionstyle.domain.entity.Person;

public class UserDTO {

  private String id;
  private String email;
  private String password;
  private Person person;

  public UserDTO(String id, String email, String password, Person person) {
    super();
    this.id = id;
    this.email = email;
    this.password = password;
    this.person = person;
  }

  public UserDTO() {}

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
    return "UserDTO [id="
        + id
        + ", email="
        + email
        + ", password="
        + password
        + ", person="
        + person
        + "]";
  }
}
