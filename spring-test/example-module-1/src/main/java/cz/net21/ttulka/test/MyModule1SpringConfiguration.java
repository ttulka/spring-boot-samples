package cz.net21.ttulka.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyModule1SpringConfiguration {

    @Value("${module.name}")
    private String moduleName;

    @Bean
    public MyModule1 myModule1(@Value("${module.value}") String moduleValue) {
        return new MyModule1(moduleName, moduleValue);
    }
}
