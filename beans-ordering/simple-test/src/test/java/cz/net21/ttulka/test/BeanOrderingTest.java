package cz.net21.ttulka.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ComponentScan("cz.net21.ttulka.test")
public class BeanOrderingTest {

    @Autowired
    private MyResource myResource;

    @Test
    public void test() {
        log.debug(String.format("Test with the resource '%s' is running...", myResource.getClass().getName()));

        assertEquals("special", myResource.getName());
        assertEquals(1, MyResourcesInitLogHolder.list().size());
        assertEquals("special", MyResourcesInitLogHolder.list().get(0));
    }
}