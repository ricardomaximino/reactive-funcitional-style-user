package com.brasajava.routerfunctionstyle.api.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.brasajava.routerfunctionstyle.api.dto.UserDTO;
import com.brasajava.routerfunctionstyle.api.dto.UserIDDTO;
import com.brasajava.routerfunctionstyle.domain.entity.User;

@Component
public class UserConverter {

  public UserDTO toUserDto(User user) {
    UserDTO dto = new UserDTO();
    BeanUtils.copyProperties(user, dto);
    return dto;
  }

  public User toUser(UserDTO dto) {
    User user = new User();
    BeanUtils.copyProperties(dto, user);
    return user;
  }

  public UserIDDTO toUserIdDto(User user) {
    UserIDDTO userIDDTO = new UserIDDTO();
    userIDDTO.setUserId(user.getId());
    return userIDDTO;
  }
}
