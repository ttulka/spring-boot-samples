package cz.net21.ttulka.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class CyclicDependenciesTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    public void testContextLoads() {
        assertThat(this.ctx).isNotNull();
        assertThat(this.ctx.containsBean("myBeanA")).isTrue();
        assertThat(this.ctx.containsBean("myBeanB")).isTrue();
    }
}
