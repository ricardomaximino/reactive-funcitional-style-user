package com.brasajava.routerfunctionstyle.service;

import com.brasajava.routerfunctionstyle.domain.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> create(User user, String operator);

  Mono<User> update(String id, User person, String operator);

  Mono<Void> deleteById(String id, String operator);

  Flux<User> findAll();

  Mono<User> findById(String id);
}
