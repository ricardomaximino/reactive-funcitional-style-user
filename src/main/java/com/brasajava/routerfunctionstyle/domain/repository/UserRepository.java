package com.brasajava.routerfunctionstyle.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.brasajava.routerfunctionstyle.domain.entity.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {}
