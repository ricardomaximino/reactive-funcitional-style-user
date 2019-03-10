package com.brasajava.routerfunctionstyle.api.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.brasajava.routerfunctionstyle.api.converter.UserConverter;
import com.brasajava.routerfunctionstyle.api.dto.UserDTO;
import com.brasajava.routerfunctionstyle.service.UserService;

import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
  private static final Logger LOG = LoggerFactory.getLogger(UserHandler.class);

  private static final String ID_PARAM = "id";
  private static final String X_USER = "X-User";

  private UserService service;
  private UserConverter converter;

  public UserHandler(UserService service, UserConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  public Mono<ServerResponse> findAll(ServerRequest request) {
    LOG.debug("FIND ALL FROM HANDLER");
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.findAll().map(converter::toUserDto), UserDTO.class);
  }

  public Mono<ServerResponse> findById(ServerRequest request) {
    LOG.debug("FIND BY ID FROM HANDLER");
    return service
        .findById(request.pathVariable(ID_PARAM))
        .flatMap(
            user ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(converter.toUserDto(user))))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    LOG.debug("CREATE FROM HANDLER");
    return request
        .bodyToMono(UserDTO.class)
        .flatMap(
            dto ->
                service
                    .create(converter.toUser(dto), request.headers().header(X_USER).get(0))
                    .flatMap(
                        user ->
                            ServerResponse.status(HttpStatus.CREATED)
                                .body(BodyInserters.fromObject(converter.toUserIdDto(user)))));
  }

  public Mono<ServerResponse> update(ServerRequest request) {
    LOG.debug("UPDATE FROM HANDLER");
    return request
        .bodyToMono(UserDTO.class)
        .flatMap(
            dto ->
                service
                    .update(
                        request.pathVariable(ID_PARAM),
                        converter.toUser(dto),
                        request.headers().header(X_USER).get(0))
                    .flatMap(user -> ServerResponse.status(HttpStatus.NO_CONTENT).build()))
        .switchIfEmpty(ServerResponse.notFound().build())
        .onErrorResume(
            e ->
                Mono.error(
                    Exceptions.propagate(
                        new ResponseStatusException(HttpStatus.ALREADY_REPORTED))));
  }

  public Mono<ServerResponse> updateWithPatch(ServerRequest request) {
    LOG.debug("UPDATE WITH PATCH FROM HANDLER");
    return request
        .bodyToMono(UserDTO.class)
        .flatMap(
            dto ->
                service
                    .update(
                        request.pathVariable(ID_PARAM),
                        converter.toUser(dto),
                        request.headers().header(X_USER).get(0))
                    .flatMap(user -> ServerResponse.status(HttpStatus.NO_CONTENT).build()))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteById(ServerRequest request) {
    LOG.debug("DELETE FROM HANDLER");
    return service
        .deleteById(request.pathVariable(ID_PARAM), request.headers().header(X_USER).get(0))
        .flatMap(d -> ServerResponse.noContent().build())
        .switchIfEmpty(ServerResponse.noContent().build());
  }
}
