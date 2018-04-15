package com.example.microservice03;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "anotherrest", url = "http://localhost:8081/2")
public interface AnotherRest {

    @GetMapping
    String sayhello();
}
