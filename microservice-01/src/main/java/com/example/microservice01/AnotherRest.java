package com.example.microservice01;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "anotherrest", url = "http://localhost:8081")
public interface AnotherRest {

    @GetMapping
    String sayhello();
}
