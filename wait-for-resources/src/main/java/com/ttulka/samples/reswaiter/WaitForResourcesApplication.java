package com.ttulka.samples.reswaiter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import lombok.SneakyThrows;

@SpringBootApplication
public class WaitForResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitForResourcesApplication.class, args);
    }

    @Configuration
    static class Config1 {
        @Bean
        MyPostProcessor1 myPostProcessor1() {
            return new MyPostProcessor1();
        }
    }

    @Configuration
    static class Config2 {
        @Bean
        MyPostProcessor2 myPostProcessor2() {
            return new MyPostProcessor2();
        }
    }

    @Configuration
    static class WaitForResourceConfig {
        @Bean
        WaitForResource waitForResource() {
            return new WaitForResource();
        }
    }
}

class WaitForResource implements BeanFactoryPostProcessor, Ordered {

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("waiting for a resource...BEGIN");
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            System.out.println("...");
        }
        System.out.println("waiting for a resource...END");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

class MyPostProcessor1 implements BeanFactoryPostProcessor, Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println(this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

class MyPostProcessor2 implements BeanFactoryPostProcessor, Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println(this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}