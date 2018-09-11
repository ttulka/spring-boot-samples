package cz.net21.ttulka.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ComponentScan("cz.net21.ttulka.test")
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class) // necessary for loading application.yml
@Slf4j
public class MyTest {

    @Value("${test.name}")
    private String testName;

    @Autowired
    private MyModule1 myModule1;

    @Test
    public void test() {
        log.debug(String.format("Test '%s' is running...", testName));

        assertEquals("my-test-name", myModule1.getName());
        assertEquals("my-test-value", myModule1.getValue());
    }
}