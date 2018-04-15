package com.example.microservice02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping
public class HelloWorld {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    @Autowired
    private Producer source;


    @GetMapping
    public String hello(HttpServletRequest request) {
        System.err.println("11111111111111111111111111111111");
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String s = headerNames.nextElement();
            LOGGER.error(s + " = " + request.getHeader(s));
        }

        try {
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        source.send();
        return "hello the world";
    }

    @GetMapping(value = "/2")
    public String hello2(HttpServletRequest request) {
        System.err.println("22222222222222222222222222222222");
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String s = headerNames.nextElement();
            LOGGER.error(s + " = " + request.getHeader(s));
        }

        try {
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello the world";
    }
}
