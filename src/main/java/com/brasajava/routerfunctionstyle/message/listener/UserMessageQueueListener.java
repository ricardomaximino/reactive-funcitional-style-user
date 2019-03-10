package com.brasajava.routerfunctionstyle.message.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;

import com.brasajava.routerfunctionstyle.message.model.SearchPersonEvent;

@EnableAutoConfiguration
@EnableBinding(Sink.class)
public class UserMessageQueueListener {
  private static final Logger LOG = LoggerFactory.getLogger(UserMessageQueueListener.class);
  private ResponseStreamer responses;

  public UserMessageQueueListener(ResponseStreamer responses) {
    this.responses = responses;
  }

  @StreamListener(Sink.INPUT)
  public void createdEvent(@Payload SearchPersonEvent event) {
    LOG.info("RECEIVE ANSWER EVENT");
    responses.getProcessor().onNext(event);
  }
}
