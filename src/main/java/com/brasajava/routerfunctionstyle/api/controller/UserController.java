package com.brasajava.routerfunctionstyle.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.data.rest.webmvc.json.patch.PatchException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brasajava.routerfunctionstyle.api.converter.UserConverter;
import com.brasajava.routerfunctionstyle.api.dto.UserDTO;
import com.brasajava.routerfunctionstyle.api.dto.UserIDDTO;
import com.brasajava.routerfunctionstyle.exception.PersonPatchNotFoundFieldException;
import com.brasajava.routerfunctionstyle.exception.PersonPatchNotFoundKeyOperationException;
import com.brasajava.routerfunctionstyle.message.listener.ResponseStreamer;
import com.brasajava.routerfunctionstyle.message.model.CreateUserEvent;
import com.brasajava.routerfunctionstyle.message.model.Event;
import com.brasajava.routerfunctionstyle.message.model.SearchPersonEvent;
import com.brasajava.routerfunctionstyle.message.sender.UserMessageQueueSender;
import com.brasajava.routerfunctionstyle.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/controller/user")
public class UserController {
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  private UserService service;
  private UserConverter converter;
  private ObjectMapper objectMapper;
  private UserMessageQueueSender sender;
  private ResponseStreamer responses;

  public UserController(
      UserService service,
      UserConverter personConverter,
      ObjectMapper objectMapper,
      UserMessageQueueSender sender,
      ResponseStreamer responses) {
    this.service = service;
    this.converter = personConverter;
    this.objectMapper = objectMapper;
    this.sender = sender;
    this.responses = responses;
  }

  @GetMapping("/person")
  @ResponseStatus(HttpStatus.OK)
  public Flux<SearchPersonEvent> getAll() {
    LOG.debug("FIND ALL FROM CONTROLLER");
    sender.sendEvent(
        MessageBuilder.withPayload(
                (Event)
                    new CreateUserEvent(
                        "id",
                        "d6e3610628ea4305965840494abbbdb3",
                        "user",
                        new Date().getTime(),
                        null))
            .setHeader("routingKey", "brasajava.user.search.request")
            .build());
    return responses.getProcessor();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<UserDTO> getPerson() {
    LOG.debug("FIND PERSON INFO FROM CONTROLLER");
    return service.findAll().map(converter::toUserDto);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<UserDTO>> getById(@PathVariable("id") String id) {
    LOG.debug("FIND BY ID FROM CONTROLLER");
    return service
        .findById(id)
        .map(u -> ResponseEntity.ok(converter.toUserDto(u)))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ResponseEntity<UserIDDTO>> create(
      @Valid @RequestBody UserDTO userDTO, @RequestHeader("X-User") String operator) {
    LOG.debug("CREATE FROM CONTROLLER");
    return service
        .create(converter.toUser(userDTO), operator)
        .map(u -> new ResponseEntity<>(converter.toUserIdDto(u), HttpStatus.CREATED));
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<String>> update(
      @Valid @RequestBody UserDTO userDTO,
      @PathVariable("id") String id,
      @RequestHeader("X-User") String operator) {
    LOG.debug("UPDATE FROM CONTROLLER");
    return service
        .update(id, converter.toUser(userDTO), operator)
        .map(u -> new ResponseEntity<String>(HttpStatus.NO_CONTENT))
        .defaultIfEmpty(new ResponseEntity<String>(HttpStatus.NOT_FOUND));
  }

  @PatchMapping("/{id}")
  public Mono<ResponseEntity<String>> updateWithPatch(
      @PathVariable("id") String id,
      @RequestBody Object objectPatch,
      @RequestHeader("X-User") String operator) {
    LOG.debug("UPDATE WITH PATCH FROM CONTROLLER");
    return service
        .findById(id)
        .map(converter::toUserDto)
        .map(dto -> applyPatch(dto, objectPatch))
        .flatMap(dto -> service.update(id, converter.toUser(dto), operator))
        .map(u -> new ResponseEntity<String>(HttpStatus.NO_CONTENT))
        .defaultIfEmpty(new ResponseEntity<String>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteById(
      @PathVariable("id") String id, @RequestHeader("X-User") String operator) {
    LOG.debug("DELETE FROM CONTROLLER");
    return service.deleteById(id, operator);
  }

  private UserDTO applyPatch(UserDTO userDTO, Object objectPatch) {
    Patch patch = convertRequestToPatch(objectPatch);
    try {
      patch.apply(userDTO, UserDTO.class);
    } catch (PropertyReferenceException | PatchException | SpelEvaluationException ex) {
      throw new PersonPatchNotFoundFieldException();
    }
    return userDTO;
  }

  private Patch convertRequestToPatch(Object objectPatch) {
    JsonNode node = objectMapper.convertValue(objectPatch, JsonNode.class);
    Patch patch;
    try {
      patch = new JsonPatchPatchConverter(objectMapper).convert(node);
    } catch (NullPointerException ex) {
      throw new PersonPatchNotFoundKeyOperationException();
    }
    return patch;
  }
}
