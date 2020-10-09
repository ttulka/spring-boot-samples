package com.ttulka.samples; /* a different package necessary for the test configuration not to be scanned */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class DeserializationTest {

    private static final String SERVICE_PATH = "/service/sample";

    private static final Path HACKED_FILE = Paths.get("./HACKED");

    @SpringBootTest(properties = "safe-deserialization=true", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @Nested
    class UnsafeDeserializationTest {

        @Test
        void deserialization_is_unsafe(@LocalServerPort int port) {
            given()
                .port(port)
                .contentType("application/x-java-serialized-object")
                .body(this.getClass().getResourceAsStream("/beanutils1.ser")).
            when()
                .post(SERVICE_PATH).
            then()
                .statusCode(500);

            assertThat(Files.exists(HACKED_FILE)).isTrue().as("Exploit was not successful");
        }
    }

    @SpringBootTest(properties = "safe-deserialization=true", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @Nested
    class SafeDeserializationTest {

        @Test
        void deserialization_is_safe(@LocalServerPort int port) {
            given()
                .port(port)
                .contentType("application/x-java-serialized-object")
                .body(this.getClass().getResourceAsStream("/beanutils1.ser")).
            when()
                .post(SERVICE_PATH).
            then()
                .statusCode(500);

            assertThat(Files.exists(HACKED_FILE)).isFalse().as("Exploit was successful");
        }
    }

    @AfterEach
    void deleteHackedFile() throws IOException {
        Files.deleteIfExists(HACKED_FILE);
    }

//    @ContextConfiguration
//    static class ClientConfig {
//
//        @Bean
//        public HttpInvokerProxyFactoryBean invoker() {
//            HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
//            invoker.setServiceUrl(SERVICE_URL);
//            invoker.setServiceInterface(SampleService.class);
//            return invoker;
//        }
//    }
}

