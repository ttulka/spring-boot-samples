package com.ttulka.samples.jta.spring.atomikos;

import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.ttulka.samples.jta.spring.atomikos.model1.SampleEntity1;
import com.ttulka.samples.jta.spring.atomikos.model2.SampleEntity2;

@Configuration
public class JtaHibernateConfig {

    @Bean("unit1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(EntityManagerFactoryBuilder builder, DataSource dataSource, JtaTransactionManager jtaTransactionManager) {
        return builder.dataSource(dataSource)
                .properties(vendorProperties(jtaTransactionManager))
                .persistenceUnit("unit1")
                .mappingResources("META-INF/orm-unit1.xml")
                .packages(SampleEntity1.class)
                .jta(true)
                .build();
    }

    @Bean("unit2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder, DataSource dataSource, JtaTransactionManager jtaTransactionManager) {
        return builder.dataSource(dataSource)
                .properties(vendorProperties(jtaTransactionManager))
                .persistenceUnit("unit2")
                .mappingResources("META-INF/orm-unit2.xml")
                .packages(SampleEntity2.class)
                .jta(true)
                .build();
    }

    @Bean
    Map<String, Object> vendorProperties(JtaTransactionManager jtaTransactionManager) {
        return Collections.singletonMap("hibernate.transaction.jta.platform", new SpringJtaPlatform(jtaTransactionManager));
    }
}
