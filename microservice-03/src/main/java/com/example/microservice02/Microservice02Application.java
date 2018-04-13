package com.example.microservice02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@EnableBinding(Sink.class)
@SpringBootApplication
public class Microservice02Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice02Application.class, args);
	}

	@StreamListener(Sink.INPUT)
	public void loggerSink(Message<?> content) {
		System.err.println("Received: " + content);
	}
}
