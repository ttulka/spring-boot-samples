package com.ttulka.samples;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApplication.class)
                .properties("spring.mvc.log-request-details=true")
                .run(args);
    }

    @Bean
    MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter(
                new Jackson2ObjectMapperBuilder()
                    .defaultUseWrapper(false)
                    .createXmlMapper(true)
                    .build()
        );
    }
}
