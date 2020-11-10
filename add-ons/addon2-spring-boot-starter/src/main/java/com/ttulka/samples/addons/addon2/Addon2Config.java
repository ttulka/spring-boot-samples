package com.ttulka.samples.addons.addon2;

import com.ttulka.samples.addons.MyService;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

// see https://www.baeldung.com/spring-yaml-propertysource for YAML support
@PropertySource("classpath:addon2.properties")
@Configuration
@EnableConfigurationProperties(Addon2Config.Addon2Properties.class)
class Addon2Config {

    @Bean
    MyController2 myController2(MyService myService, Addon2Properties properties) {
        return new MyController2(myService, properties.getName());
    }

    @ConfigurationProperties(prefix = "addons.addon2")
    @Getter
    @Setter
    static class Addon2Properties {

        private String name;
    }
}
