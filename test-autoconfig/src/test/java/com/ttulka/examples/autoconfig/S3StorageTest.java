package com.ttulka.examples.autoconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("aws")
@Import(S3StorageTest.S3StorageMocksTestConfiguration.class)
class S3StorageTest extends AbstractStorageTest {

    @Autowired
    private String s3Bean;

    @Test
    void s3SpecificTest() {
        assertThat(s3Bean).isEqualTo("s3bean");
    }

    @TestConfiguration
    static class S3StorageMocksTestConfiguration {
        @Bean
        String s3Bean() {
            return "s3bean";
        }
    }
}
