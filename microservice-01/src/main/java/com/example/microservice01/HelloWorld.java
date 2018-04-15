package com.example.microservice01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        return "hello the world";
    }
}
