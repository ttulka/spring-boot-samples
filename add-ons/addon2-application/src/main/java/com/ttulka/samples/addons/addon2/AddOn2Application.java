package com.ttulka.samples.addons.addon2;

import com.ttulka.samples.addons.MyService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AddOn2Application {

    public static void main(String[] args) {
        SpringApplication.run(AddOn2Application.class, args);
    }

    @Bean
    MyService fakeMyService() {
        return getClass()::getName;
    }
}
