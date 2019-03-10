package com.brasajava.routerfunctionstyle.message.model;

public class UpdateUserEvent extends Event {

  public UpdateUserEvent() {
    super();
  }

  public UpdateUserEvent(String id, String key, String user, Long creationDate, Object object) {
    super(id, "user", "updated", key, user, creationDate, object);
  }
}
