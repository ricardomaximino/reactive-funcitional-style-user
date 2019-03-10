package com.brasajava.routerfunctionstyle.message.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.messaging.Message;

import com.brasajava.routerfunctionstyle.message.model.Event;

import reactor.core.publisher.DirectProcessor;

@EnableAutoConfiguration
@EnableBinding(Source.class)
public class UserMessageQueueSender {

  private static final Logger LOG = LoggerFactory.getLogger(UserMessageQueueSender.class);
  private FluxSender fluxSender;

  public DirectProcessor<Message<Event>> fluxMessage = DirectProcessor.create();

  @StreamEmitter
  public void setFluxSender(@Output(Source.OUTPUT) FluxSender fluxSender) {
    this.fluxSender = fluxSender;
    this.fluxSender
        .send(fluxMessage)
        .subscribe(
            s -> LOG.info("USER EVENT SENT"),
            error -> LOG.info("ERROR ON SEND USER EVENT"),
            () -> LOG.info("USER EVENT SENT COMPLETE"));
  }

  public void sendEvent(Message<Event> message) {
    this.fluxMessage.onNext(message);
  }
}
