package com.ttulka.samples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteExporter;

import lombok.Getter;
import lombok.Setter;

@SpringBootApplication
@EnableConfigurationProperties(UnsafeDeserializationApplication.DeserializationProperties.class)
public class UnsafeDeserializationApplication {

    // TODO doesn't work here
    static {
        System.getProperties().setProperty("jdk.serialFilter", "com.ttulka.*;!*");
    }

    public static void main(String[] args) {
        SpringApplication.run(UnsafeDeserializationApplication.class, args);
    }

    @Bean(name = "/service/sample")
    RemoteExporter exporterServiceRemote(SampleService sampleService) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(sampleService);
        exporter.setServiceInterface(SampleService.class);
        exporter.setAcceptProxyClasses(false);
        return exporter;
    }

    @Bean
    SampleServiceImpl serviceRemoteImpl() {
        return new SampleServiceImpl();
    }

    @Autowired DeserializationProperties properties;

    @ConfigurationProperties
    @Getter
    @Setter
    static class DeserializationProperties {

        private boolean safeDeserialization = false;
    }
}
