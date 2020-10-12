package com.ttulka.samples;

import javax.annotation.PostConstruct;

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
import sun.misc.ObjectInputFilter;

@SpringBootApplication
@EnableConfigurationProperties(UnsafeDeserializationApplication.DeserializationProperties.class)
public class UnsafeDeserializationApplication {

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

    @Autowired DeserializationProperties deserializationProperties;

    @PostConstruct
    void setupSerialFilter() {
        if (deserializationProperties.isSafeDeserialization()) {
            // https://docs.oracle.com/javase/10/core/serialization-filtering1.htm
            ObjectInputFilter filter = ObjectInputFilter.Config.createFilter("com.ttulka.*;!*");
            ObjectInputFilter.Config.setSerialFilter(filter);
        }
    }

    @Autowired DeserializationProperties properties;

    @ConfigurationProperties
    @Getter
    @Setter
    static class DeserializationProperties {

        private boolean safeDeserialization = false;
    }
}
