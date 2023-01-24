package com.ttulka.examples.autoconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("azure")
@Import(AzureStorageTest.AzureStorageMocksTestConfiguration.class)
class AzureStorageTest extends AbstractStorageTest {

    @Autowired
    private String azureBean;

    @Test
    void azureSpecificTest() {
        assertThat(azureBean).isEqualTo("azurebean");
    }

    @TestConfiguration
    static class AzureStorageMocksTestConfiguration {
        @Bean
        String azureBean() {
            return "azurebean";
        }
    }
}
