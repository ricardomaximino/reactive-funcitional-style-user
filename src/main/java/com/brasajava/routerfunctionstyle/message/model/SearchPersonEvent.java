package com.brasajava.routerfunctionstyle.message.model;

public class SearchPersonEvent extends Event {

  public SearchPersonEvent() {
    super();
  }

  public SearchPersonEvent(String id, String key, String user, Long creationDate, Object object) {
    super(id, "user", "created", key, user, creationDate, object);
  }
}
