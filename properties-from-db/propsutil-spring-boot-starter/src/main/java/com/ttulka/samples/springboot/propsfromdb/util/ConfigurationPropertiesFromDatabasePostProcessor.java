package com.ttulka.samples.springboot.propsfromdb.util;

import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

class ConfigurationPropertiesFromDatabasePostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            DataSource dataSource = DataSourceBuilder
                    .create()
                    .url(environment.getProperty("spring.datasource.url"))
                    .username(environment.getProperty("spring.datasource.username"))
                    .password(environment.getProperty("spring.datasource.password"))
                    .driverClassName(environment.getProperty("spring.datasource.driver-class-name"))
                    .build();

            environment.getPropertySources().addLast(
                    new MapPropertySource(
                            "propertiesFromDatabase",
                            new Properties(
                                    environment.getProperty("util.properties.table", "properties"),
                                    new JdbcTemplate(dataSource))
                                    .all().stream()
                                    .collect(Collectors.toMap(
                                            Property::getName,
                                            Property::getValue))));

        } catch (BadSqlGrammarException ignore) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
