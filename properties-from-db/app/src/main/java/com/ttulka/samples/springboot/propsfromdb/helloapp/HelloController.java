package com.ttulka.samples.springboot.propsfromdb.helloapp;

import com.ttulka.samples.springboot.propsfromdb.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping("/hello")
@AllArgsConstructor
class HelloController {

    private final Properties properties;

    private String greeting;

    @GetMapping
    public String sayHello() {
        return this.greeting;
    }

    @PostMapping
    public ResponseEntity updateGreeting(@NonNull String greeting) {
        this.greeting = greeting;
        this.properties.store("hello.greeting", greeting);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
