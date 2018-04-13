package com.example.microservice01;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.OutboundHeaders;
import com.newrelic.api.agent.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;
import java.util.logging.Level;

@RestController
@RequestMapping
public class HelloWorld {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    @Autowired
    private AnotherRest anotherRest;

    @GetMapping
    public String hello(HttpServletRequest request) {
        LOGGER.error("now calling the other microservice-02");
        anotherRest.sayhello();
       // new RestTemplate().getForEntity("http://localhost:8081", String.class);
        return "hello the world";
    }
}
