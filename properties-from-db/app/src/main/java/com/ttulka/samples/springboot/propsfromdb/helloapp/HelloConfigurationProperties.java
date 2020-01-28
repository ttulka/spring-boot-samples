package com.ttulka.samples.springboot.propsfromdb.helloapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("hello")
@Getter
@Setter
class HelloConfigurationProperties {

    private String greeting = "Hello!";
}
