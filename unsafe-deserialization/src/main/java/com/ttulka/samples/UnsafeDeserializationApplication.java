package com.ttulka.samples;

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
@EnableConfigurationProperties(UnsafeDeserializationApplication.UnsafeDeserializationProperties.class)
public class UnsafeDeserializationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnsafeDeserializationApplication.class, args);
    }

    @Bean(name = "/service/sample")
    RemoteExporter exporterServiceRemote(SampleService sampleService, UnsafeDeserializationProperties properties) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(sampleService);
        exporter.setServiceInterface(SampleService.class);

        // https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2011-2894, https://www.div0.sg/single-post/2017/06/17/CVE-2011-2894, https://seclists.org/fulldisclosure/2011/Sep/80
        exporter.setAcceptProxyClasses(properties.isAcceptProxyClasses());

        return exporter;
    }

    @Bean
    SampleServiceImpl serviceRemoteImpl() {
        return new SampleServiceImpl();
    }

    @ConfigurationProperties
    @Getter
    @Setter
    static class UnsafeDeserializationProperties {

        private boolean acceptProxyClasses = false;
    }
}
