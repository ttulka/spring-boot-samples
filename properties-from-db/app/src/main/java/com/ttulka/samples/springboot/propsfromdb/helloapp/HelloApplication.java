package com.ttulka.samples.springboot.propsfromdb.helloapp;

import com.ttulka.samples.springboot.propsfromdb.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(HelloConfigurationProperties.class)
class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    HelloController helloController(HelloConfigurationProperties configurationProperties, Properties properties) {
        return new HelloController(properties, configurationProperties.getGreeting());
    }
}
