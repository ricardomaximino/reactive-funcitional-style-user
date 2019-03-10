package com.brasajava.routerfunctionstyle.message.model;

public class DeleteUserEvent extends Event {

  public DeleteUserEvent() {
    super();
  }

  public DeleteUserEvent(String id, String key, String user, Long creationDate, Object object) {
    super(id, "user", "deleted", key, user, creationDate, object);
  }
}
