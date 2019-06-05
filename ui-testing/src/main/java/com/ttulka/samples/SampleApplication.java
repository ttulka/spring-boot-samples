package com.ttulka.samples;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

@SpringBootConfiguration
@EnableAutoConfiguration
public class SampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApplication.class)
                .properties("spring.jmx.enabled=false",
                            "spring.mvc.log-request-details=true"
                ).run(args);
    }

    @Configuration
    @Import(IndexController.class)
    static class Config {
    }
}
