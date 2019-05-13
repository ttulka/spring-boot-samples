package com.ttulka.samples.jta.spring.atomikos;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.atomikos.eclipselink.platform.AtomikosPlatform;

import org.eclipse.persistence.platform.database.H2Platform;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.ttulka.samples.jta.spring.atomikos.model1.SampleEntity1;
import com.ttulka.samples.jta.spring.atomikos.model2.SampleEntity2;

@Configuration
public class JtaEclipseLinkConfig {

    @Bean("unit1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource)
                .properties(vendorProperties())
                .persistenceUnit("unit1")
                .mappingResources("META-INF/orm-unit1.xml")
                .packages(SampleEntity1.class)
                .jta(true)
                .build();
    }

    @Bean("unit2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource)
                .properties(vendorProperties())
                .persistenceUnit("unit2")
                .mappingResources("META-INF/orm-unit2.xml")
                .packages(SampleEntity2.class)
                .jta(true)
                .build();
    }

    @Bean
    Map<String, Object> vendorProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("eclipselink.target-server", AtomikosPlatform.class.getName());
        properties.put("eclipselink.target-database", H2Platform.class.getName());
        properties.put("eclipselink.weaving", "false");
        properties.put("eclipselink.logging.parameters", "true");
        properties.put("eclipselink.logging.level", "WARNING");
        properties.put("eclipselink.ddl-generation", "none");
        properties.put("eclipselink.jpa.uppercase-column-names", "true");
        properties.put("eclipselink.cache.shared.default", "false");
        properties.put("javax.persistence.sharedCache.mode", "NONE");
        return properties;
    }
}
