package com.ttulka.samples.swagger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class HelloController {

    @GetMapping("hello")
    public String sayHello(String name) {
        return String.format("Hello, %s!", name);
    }
}
