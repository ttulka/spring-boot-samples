package com.ttulka.samples;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The default behavior of a Spring Boot application with auto-configuration enabled.
 * <p>
 * A custom converter is set as a bean, composed by {@link HttpMessageConvertersAutoConfiguration auto-configuration for http message converters}.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SampleController.class, MessageConverterAsBeanAutoConfigTest.Config.class})
@EnableAutoConfiguration
public class MessageConverterAsBeanAutoConfigTest {

    private String url;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    public void setup(@Value("${local.server.port:8080}") int port) {
        this.url = "http://localhost:" + port + "/data";
    }

    @Test
    void testJson() {
        ResponseEntity<String> response = httpResponse(MediaType.APPLICATION_JSON);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("{\"data\":[\"sample1\",\"sample2\"]}", response.getBody());
    }

    @Test
    void testXml() {
        ResponseEntity<String> response = httpResponse(MediaType.APPLICATION_XML);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("<SampleData><data>sample1</data><data>sample2</data></SampleData>", response.getBody());
    }

    private ResponseEntity<String> httpResponse(MediaType accept) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(accept));
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(this.url, HttpMethod.GET, request, String.class);
    }

    @Configuration
    static class Config {

        @Bean
        MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
            return new MappingJackson2XmlHttpMessageConverter(
                    new Jackson2ObjectMapperBuilder().defaultUseWrapper(false).createXmlMapper(true).build()
            );
        }
    }
}
