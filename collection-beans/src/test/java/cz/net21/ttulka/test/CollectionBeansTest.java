package cz.net21.ttulka.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ComponentScan("cz.net21.ttulka.test")
public class CollectionBeansTest {

    @Autowired
    private MyResourceHolder myResourceHolder;

    @Test
    public void test() {
        log.debug(String.format("Test with the resource '%s' is running...", myResourceHolder.getMyResources()));

        assertNotNull(myResourceHolder);
        assertEquals(3, myResourceHolder.getMyResources().size());

        List<MyResource> myResources = new ArrayList<>(myResourceHolder.getMyResources());

        assertEquals("A", myResources.get(0).getName());
        assertEquals("B", myResources.get(1).getName());
        assertEquals("C", myResources.get(2).getName());
    }
}
