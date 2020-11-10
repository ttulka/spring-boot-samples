package com.ttulka.samples.addons;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@SpringBootConfiguration
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        var ac = SpringApplication.run(Application.class, args);

        System.out.println("Controllers: " + Arrays.asList(ac.getBeanNamesForAnnotation(Controller.class)));
    }

    @Bean
    MyService myService() {
        return getClass()::getName;
    }
}
