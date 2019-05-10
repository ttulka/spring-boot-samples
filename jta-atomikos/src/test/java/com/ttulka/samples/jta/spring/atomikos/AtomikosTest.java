package com.ttulka.samples.jta.spring.atomikos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.ttulka.samples.jta.spring.atomikos.model1.SampleEntity1;
import com.ttulka.samples.jta.spring.atomikos.model2.SampleEntity2;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

abstract class AtomikosTest {

    @Autowired
    private ServiceA serviceA;

    @AfterEach
    void cleanUp() {
        serviceA.truncate();
    }

    @Test
    public void testConfiguration() {
        serviceA.list1();
        serviceA.list2();
    }

    @Test
    public void testTransaction() {
        serviceA.flush();
    }

    @Test
    public void testSave() {
        serviceA.save();

        assertThat(serviceA.list1().size(), is(1));
        assertThat(serviceA.list2().size(), is(1));
    }

    @Test
    public void testNestedTransactions() {
        serviceA.remoteCall();

        assertThat(serviceA.list1().size(), is(1));
        assertThat(serviceA.list2().size(), is(1));
    }

    @Test
    public void testNestedTransactionsWithRollback() {

        try {
            serviceA.remoteExceptionalCall();
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), is("remoteException"));
        }

        assertThat(serviceA.list1().size(), is(0));
        assertThat(serviceA.list2().size(), is(0));
    }

    @Test
    public void testPersistenceFromWrongContext() {
        assertThat(serviceA.list1().size(), is(0));
        assertThat(serviceA.list2().size(), is(0));

        try {
            serviceA.wrongSchema();
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), is("Unknown entity: " + SampleEntity2.class.getName()));
        }

        assertThat(serviceA.list1().size(), is(0));
        assertThat(serviceA.list2().size(), is(0));
    }

    @Configuration
    static class Config {

        @Bean
        ServiceA serviceA() {
            return new ServiceA();
        }

        @Bean
        ServiceB serviceB() {
            return new ServiceB();
        }
    }

    @RequiredArgsConstructor
    static class ServiceA {

        @Autowired
        private ServiceB serviceB;

        @PersistenceContext(unitName = "unit1")
        private EntityManager em1;

        @PersistenceContext(unitName = "unit2")
        private EntityManager em2;

        public List<SampleEntity1> list1() {
            return em1.createQuery("select s from SampleEntity1 s", SampleEntity1.class).getResultList();
        }

        public List<SampleEntity2> list2() {
            return em2.createQuery("select s from SampleEntity2 s", SampleEntity2.class).getResultList();
        }

        @Transactional
        public void truncate() {
            em1.createQuery("delete from SampleEntity1").executeUpdate();
            em2.createQuery("delete from SampleEntity2").executeUpdate();
        }

        @Transactional
        public void flush() {
            em1.flush();
            em2.flush();
        }

        @Transactional
        public void save() {
            em1.persist(new SampleEntity1());
            em2.persist(new SampleEntity2());
        }

        @Transactional
        public void remoteCall() {
            em1.persist(new SampleEntity1());
            serviceB.remote();
        }

        @Transactional
        public void remoteExceptionalCall() {
            em1.persist(new SampleEntity1());
            serviceB.remoteException();
        }

        @Transactional
        public void wrongSchema() {
            em1.persist(new SampleEntity2());
        }
    }

    @RequiredArgsConstructor
    static class ServiceB {

        @PersistenceContext(unitName = "unit2")
        protected EntityManager em2;

        @Transactional
        public void remote() {
            em2.persist(new SampleEntity2());
        }

        @Transactional
        public void remoteException() {
            throw new RuntimeException("remoteException");
        }
    }
}
