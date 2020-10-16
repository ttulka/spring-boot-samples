package com.ttulka.samples.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@SpringBootApplication
public class SwaggerSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerSampleApplication.class, args);
    }

    @Configuration
    static class SwaggerConfig {

        @Bean
        Docket swaggerDocket() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("business-api")
                    .select()
                    .apis(basePackage(this.getClass().getPackageName()))
                    .paths(any())
                    .build()
                    .apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Swagger API Example")
                    .description("Sample API for Swagger")
                    .version("1")
                    .build();
        }
    }
}
