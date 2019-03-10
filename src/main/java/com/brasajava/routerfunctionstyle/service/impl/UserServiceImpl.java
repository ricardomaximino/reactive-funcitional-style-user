package com.brasajava.routerfunctionstyle.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.brasajava.routerfunctionstyle.domain.entity.User;
import com.brasajava.routerfunctionstyle.domain.repository.UserRepository;
import com.brasajava.routerfunctionstyle.message.model.CreateUserEvent;
import com.brasajava.routerfunctionstyle.message.model.DeleteUserEvent;
import com.brasajava.routerfunctionstyle.message.model.UpdateUserEvent;
import com.brasajava.routerfunctionstyle.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository repository;
  private ApplicationEventPublisher eventPublisher;

  public UserServiceImpl(UserRepository repository, ApplicationEventPublisher eventPublisher) {
    this.repository = repository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Mono<User> create(User user, String operator) {
    user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    return repository.save(onCreate(user, operator));
  }

  @Override
  public Mono<User> update(String id, User user, String operator) {
    return repository
        .findById(id)
        .flatMap(
            u -> {
              return repository.save(onUpdate(u, user, operator));
            });
  }

  @Override
  public Mono<Void> deleteById(String id, String operator) {
    return repository
        .findById(id)
        .flatMap(
            user -> {
              return repository.delete(user).doOnSuccess(v -> afterDelete(user, operator));
            });
  }

  @Override
  public Flux<User> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<User> findById(String id) {
    return repository.findById(id);
  }

  private User onCreate(User user, String operator) {
    user.create(
        new CreateUserEvent(
            UUID.randomUUID().toString(), user.getId(), operator, new Date().getTime(), user));
    return user;
  }

  private User onUpdate(User userDB, User user, String operator) {
    user.setId(userDB.getId());
    user.update(
        new UpdateUserEvent(
            UUID.randomUUID().toString(), user.getId(), operator, new Date().getTime(), user));
    return user;
  }

  private void afterDelete(User user, String operator) {
    eventPublisher.publishEvent(
        new DeleteUserEvent(
            UUID.randomUUID().toString(), user.getId(), operator, new Date().getTime(), user));
    user = null;
  }
}
