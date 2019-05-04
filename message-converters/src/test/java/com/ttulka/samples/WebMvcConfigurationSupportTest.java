package com.ttulka.samples;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Auto-configuration disabled. {@link EnableWebMvc standard web configuration} removed - just one custom {@link WebMvcConfigurationSupport web configuration)
 * must be provided.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SampleController.class, WebMvcConfigurationSupportTest.WebConfig.class})
@EnableAutoConfiguration(
        exclude = {WebMvcAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
public class WebMvcConfigurationSupportTest {

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
    static class WebConfig extends WebMvcConfigurationSupport {

        // Doesn't work (JSON test fails) - default converters are overwritten
//        @Override
//        protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//            converters.add(new MappingJackson2XmlHttpMessageConverter(
//                    new Jackson2ObjectMapperBuilder().defaultUseWrapper(false).createXmlMapper(true).build()
//            ));
//        }

        @Override
        protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            // The `extendMessageConverters` method is called after the default converters are set.
            // To overwrite the default, a custom converter must be put at the beginning of the list.
            converters.add(0, new MappingJackson2XmlHttpMessageConverter(
                    new Jackson2ObjectMapperBuilder().defaultUseWrapper(false).createXmlMapper(true).build()
            ));
        }
    }
}
