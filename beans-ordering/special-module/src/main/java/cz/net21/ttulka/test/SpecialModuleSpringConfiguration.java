package cz.net21.ttulka.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SpecialModuleSpringConfiguration {

    @Bean
    public MyResource myResource() {
        MyResourcesInitLogHolder.add("special");
        return new MyResourceSpecial("special");
    }
}
