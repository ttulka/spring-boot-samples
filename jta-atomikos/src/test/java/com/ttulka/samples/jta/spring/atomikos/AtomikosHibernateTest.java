package com.ttulka.samples.jta.spring.atomikos;

import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EclipselinkJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JtaHibernateConfig.class})
@EnableAutoConfiguration(exclude = {EclipselinkJpaAutoConfiguration.class})
public class AtomikosHibernateTest extends AtomikosTest {

    @Autowired
    AtomikosHibernateTest(Dao1 dao1, Dao2 dao2, TransactionManager transactionManager) {
        super(dao1, dao2, transactionManager);
    }
}
