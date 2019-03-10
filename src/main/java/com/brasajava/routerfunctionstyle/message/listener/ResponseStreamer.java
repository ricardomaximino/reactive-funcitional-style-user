package com.brasajava.routerfunctionstyle.message.listener;

import org.springframework.stereotype.Component;

import com.brasajava.routerfunctionstyle.message.model.SearchPersonEvent;

import reactor.core.publisher.DirectProcessor;

@Component
public class ResponseStreamer {

  private DirectProcessor<SearchPersonEvent> responses = DirectProcessor.create();

  public DirectProcessor<SearchPersonEvent> getProcessor() {
    return this.responses;
  }
}
