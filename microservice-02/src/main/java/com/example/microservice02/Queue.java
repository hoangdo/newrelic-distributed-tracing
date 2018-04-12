package com.example.microservice02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;

@EnableBinding(Source.class)
@Configuration
public class Queue {

    @Autowired
    private Source source;

    public void send() {
        source.output().send(MessageBuilder.withPayload("a hello world").build());
    }
}
