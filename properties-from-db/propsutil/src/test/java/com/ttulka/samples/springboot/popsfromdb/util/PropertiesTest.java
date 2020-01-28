package com.ttulka.samples.springboot.popsfromdb.util;

import java.util.Optional;
import java.util.Set;

import com.ttulka.samples.springboot.propsfromdb.util.Properties;
import com.ttulka.samples.springboot.propsfromdb.util.Property;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Transactional
class PropertiesTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Properties properties;

    @BeforeEach
    void setup() {
        this.properties = new Properties("properties", jdbcTemplate);
    }

    @Test
    void property_is_found_after_added() {
        properties.update("test", "test_value");

        Optional<Property> found = properties.byName("test");

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(new Property("test", "test_value"));
    }

    @Test
    void non_existing_property_is_not_found() {
        Optional<Property> found = properties.byName("test");

        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void property_is_updated() {
        properties.update("test", "test_value");
        properties.update("test", "test_value_updated");

        Optional<Property> found = properties.byName("test");

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(new Property("test", "test_value_updated"));
    }

    @Test
    void all_added_properties_are_found() {
        properties.update("test1", "test_value1");
        properties.update("test2", "test_value2");
        properties.update("test3", "test_value3");

        Set<Property> found = properties.all();

        assertThat(found).containsExactlyInAnyOrder(
                new Property("test1", "test_value1"),
                new Property("test2", "test_value2"),
                new Property("test3", "test_value3")
        );
    }

    @Configuration
    static class TestConfig {
    }
}
