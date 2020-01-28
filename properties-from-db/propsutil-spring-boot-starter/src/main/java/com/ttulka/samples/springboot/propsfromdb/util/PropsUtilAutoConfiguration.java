package com.ttulka.samples.springboot.propsfromdb.util;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(PropsUtilConfigurationProperties.class)
public class PropsUtilAutoConfiguration {

    @Bean
    Properties properties(PropsUtilConfigurationProperties propsUtilConfigurationProperties, JdbcTemplate jdbcTemplate) {
        return new Properties(propsUtilConfigurationProperties.getTable(), jdbcTemplate);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}
