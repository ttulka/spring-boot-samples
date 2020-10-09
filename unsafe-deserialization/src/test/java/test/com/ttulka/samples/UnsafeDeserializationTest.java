package test.com.ttulka.samples; /* a different package necessary for the test configuration not to be scanned */

import com.ttulka.samples.SampleRequest;
import com.ttulka.samples.SampleResponse;
import com.ttulka.samples.SampleService;
import com.ttulka.samples.UnsafeDeserializationApplication;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

class UnsafeDeserializationTest {

    private static final String SERVICE_URL = "http://localhost:8080/service/sample";

    @SpringBootTest(properties = "acceptProxyClasses=true", classes = ClientConfig.class)
    @Nested
    class ProxyClassesAcceptedTest {

        @Test
        void service_via_http_invoker(@Autowired SampleService service) {
            assertThat(service.toString()).contains(SERVICE_URL);
        }

        @Test
        void service_responses_correctly(@Autowired SampleService service) {
            ConfigurableApplicationContext ac = null;
            try {
                // run the application under test
                ac = SpringApplication.run(UnsafeDeserializationApplication.class);

                SampleResponse response = service.toUpperCase(new SampleRequest("abc"));

                assertThat(response).isEqualTo(new SampleResponse("ABC"));

            } finally {
                if (ac != null) {
                    ac.close();
                }
            }
        }

        @Test
        void deserialization_is_unsafe(@Autowired SampleService service) {
            ConfigurableApplicationContext ac = null;
            try {
                // run the application under test
                ac = SpringApplication.run(UnsafeDeserializationApplication.class);

                SampleResponse response = service.toUpperCase(new SampleRequest("abc"));

                assertThat(response).isEqualTo(new SampleResponse("ABC"));

            } finally {
                if (ac != null) {
                    ac.close();
                }
            }

            // TODO
        }
    }

//    @SpringBootTest(properties = "acceptProxyClasses=false", classes = ClientConfig.class)
//    @Nested
//    class ProxyClassesNotAcceptedTest {
//
//        @Test
//        void deserialization_is_safe(@Autowired SampleService service) {
//            // TODO
//        }
//    }

    @ContextConfiguration
    static class ClientConfig {

        @Bean
        public HttpInvokerProxyFactoryBean invoker() {
            HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
            invoker.setServiceUrl(SERVICE_URL);
            invoker.setServiceInterface(SampleService.class);
            return invoker;
        }
    }
}
