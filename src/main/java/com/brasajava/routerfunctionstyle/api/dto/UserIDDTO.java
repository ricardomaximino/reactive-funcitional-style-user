package com.brasajava.routerfunctionstyle.api.dto;

public class UserIDDTO {
  private String userId;

  public UserIDDTO() {}

  public UserIDDTO(String userId) {
    super();
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "UserIDDTO [userId=" + userId + "]";
  }
}
