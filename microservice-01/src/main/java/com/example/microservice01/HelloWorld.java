package com.example.microservice01;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping
public class HelloWorld {

    private RestTemplate rt = new RestTemplate();

    @GetMapping
    public String hello() {
        final String value = UUID.randomUUID().toString();
        System.err.println(value);
        NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(new OutboundHeaders() {
            @Override
            public HeaderType getHeaderType() {
                return HeaderType.HTTP;
            }

            @Override
            public void setHeader(String s, String s1) {

            }
        });

        rt.getForEntity("http://localhost:8081", String.class);
        return "hello the world";
    }
}
