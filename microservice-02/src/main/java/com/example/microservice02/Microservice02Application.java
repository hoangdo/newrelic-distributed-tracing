package com.example.microservice02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
public class Microservice02Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice02Application.class, args);
	}
}
