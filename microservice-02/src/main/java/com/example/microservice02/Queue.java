package com.example.microservice02;

import com.newrelic.api.agent.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;

import java.net.URI;
import java.util.Arrays;

@EnableBinding(Source.class)
@Configuration
public class Queue {

    @Autowired
    private Source source;

    @Trace
    public void send() {
        MessageBuilder<String> a_hello_world = MessageBuilder.withPayload("a hello world");
        Arrays.asList(Microservice02Application.CONTEXTUAL_HEADERS).forEach(h -> a_hello_world.setHeader(h, MDC.get(h)));

        ExternalParameters params = MessageProduceParameters
            .library("kafa")
            .destinationType(DestinationType.NAMED_TOPIC)
            .destinationName("hello")
            .outboundHeaders(new OutboundHeaders() {
                @Override
                public HeaderType getHeaderType() {
                    return HeaderType.MESSAGE;
                }

                @Override
                public void setHeader(String s, String s1) {

                }
            })
            .build();
        NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
        source.output().send(a_hello_world.build());
    }
}
