package com.brasajava.routerfunctionstyle.message.model;

public class CreateUserEvent extends Event {

  public CreateUserEvent() {
    super();
  }

  public CreateUserEvent(String id, String key, String user, Long creationDate, Object object) {
    super(id, "user", "created", key, user, creationDate, object);
  }
}
