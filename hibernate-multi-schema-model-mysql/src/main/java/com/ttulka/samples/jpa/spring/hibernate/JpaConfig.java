package com.ttulka.samples.jpa.spring.hibernate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JpaConfig {

    @Bean
    DefaultPersistenceUnitManager persistenceUnitManager(DataSource dataSource) {
        DefaultPersistenceUnitManager pum = new DefaultPersistenceUnitManager();
        pum.setPersistenceXmlLocations("classpath*:META-INF/persistence*.xml");
        pum.setDefaultDataSource(dataSource);
        pum.setDefaultJtaDataSource(null);
        pum.afterPropertiesSet();
        return pum;
    }

    @Bean("unit1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource)
                .persistenceUnit("unit1")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager1(@Qualifier("unit1") LocalContainerEntityManagerFactoryBean domain) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(domain.getObject());
        tm.setPersistenceUnitName("unit1");
        tm.afterPropertiesSet();
        return tm;
    }

    @Bean("unit2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource)
                .persistenceUnit("unit2")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager2(@Qualifier("unit2") LocalContainerEntityManagerFactoryBean domain) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(domain.getObject());
        tm.setPersistenceUnitName("unit2");
        tm.afterPropertiesSet();
        return tm;
    }
}
