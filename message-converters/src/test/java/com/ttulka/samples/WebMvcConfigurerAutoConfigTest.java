package com.ttulka.samples;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The default behavior of a Spring Boot application with auto-configuration enabled and a custom {@link WebMvcConfigurer configurer}.
 * <p>
 * The {@link WebMvcAutoConfiguration web auto-configuration} provides a default {@link WebMvcConfigurer configurer} (this is where the default converters come
 * from).
 * <p>
 * The imported {@link DelegatingWebMvcConfiguration web configuration} collects instances of the {@link WebMvcConfigurer configurer}.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SampleController.class, WebMvcConfigurerAutoConfigTest.Config.class})
@EnableAutoConfiguration
public class WebMvcConfigurerAutoConfigTest {

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
    static class Config implements WebMvcConfigurer {

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            // To overwrite the default, the custom converter must be put at the beginning of the list of collected converters.
            converters.add(0, new MappingJackson2XmlHttpMessageConverter(
                    new Jackson2ObjectMapperBuilder().defaultUseWrapper(false).createXmlMapper(true).build()
            ));
        }
    }
}
