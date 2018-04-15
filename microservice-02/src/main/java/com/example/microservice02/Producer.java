package com.example.microservice02;

import com.newrelic.api.agent.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;

@EnableBinding(Source.class)
@Configuration
public class Producer {

    @Autowired
    private Source source;

    @Trace
    public void send() {
        MessageBuilder<String> a_hello_world = MessageBuilder.withPayload("a hello world");
        source.output().send(a_hello_world.build());
    }
}
