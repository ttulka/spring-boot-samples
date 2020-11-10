package com.ttulka.samples.addons.addon1;

import com.ttulka.samples.addons.MyService;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

// see https://www.baeldung.com/spring-yaml-propertysource for YAML support
@PropertySource("classpath:addon1.properties")
@Configuration
@EnableConfigurationProperties(Addon1Config.Addon1Properties.class)
class Addon1Config {

    @Bean
    MyController1 myController1(MyService myService, Addon1Properties properties) {
        return new MyController1(myService, properties.getName());
    }

    @ConfigurationProperties(prefix = "addons.addon1")
    @Getter
    @Setter
    static class Addon1Properties {

        private String name;
    }
}
