package com.ttulka.samples.jta.spring.atomikos;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JtaHibernateConfig.class, AtomikosHibernateTest.Config.class})
@EnableAutoConfiguration
public class AtomikosHibernateTest extends AtomikosTest {
}
