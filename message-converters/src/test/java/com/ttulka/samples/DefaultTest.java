package com.ttulka.samples;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The default behavior of a Spring Boot application with auto-configuration enabled.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SampleController.class)
@EnableAutoConfiguration
public class DefaultTest {

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
        assertEquals("<SampleData><data><data>sample1</data><data>sample2</data></data></SampleData>", response.getBody());
    }

    private ResponseEntity<String> httpResponse(MediaType accept) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(accept));
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(this.url, HttpMethod.GET, request, String.class);
    }
}
