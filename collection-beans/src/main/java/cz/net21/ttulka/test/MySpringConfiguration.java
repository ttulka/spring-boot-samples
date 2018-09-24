package cz.net21.ttulka.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MySpringConfiguration {

    @Bean
    public MyResource myX() {
        return new MyResource("X");
    }

    @Bean
    public List<MyResource> myResources() {
        return Arrays.asList(new MyResource("A"), new MyResource("B"), new MyResource("C"));
    }

    @Bean
    public Collection<MyResource> myNumbers() {
        return Arrays.asList(new MyResource("1"), new MyResource("2"));
    }

    @Bean
    public MyResource myY() {
        return new MyResource("Y");
    }

    @Bean
    public MyResourceHolder myResourceHolder(@Qualifier("myResources") List<MyResource> myResources) {
        return new MyResourceHolder(myResources);
    }
}
