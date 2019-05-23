package com.ttulka.samples.jpa.spring.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ttulka.samples.jpa.spring.hibernate.model1.SampleEntity1;
import com.ttulka.samples.jpa.spring.hibernate.model2.SampleEntity2;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JpaConfig.class, HibernateMultiSchemaTest.Dao1.class, HibernateMultiSchemaTest.Dao2.class})
@EnableAutoConfiguration
class HibernateMultiSchemaTest {

    @Autowired
    private Dao1 dao1;
    @Autowired
    private Dao2 dao2;

    @Test
    public void insertTest() {
        dao1.insert();
        dao1.insert();

        dao2.insert();
        dao2.insert();

        assertThat(dao1.listAll()).hasSize(2);
        assertThat(dao2.listAll()).hasSize(2);
    }

    @RequiredArgsConstructor
    static class Dao1 {

        @PersistenceContext(unitName = "unit1")
        private EntityManager em1;

        public List<SampleEntity1> listAll() {
            return em1.createQuery("select s from SampleEntity1 s", SampleEntity1.class).getResultList();
        }

        @Transactional("transactionManager1")
        public void insert() {
            em1.persist(new SampleEntity1());
            em1.flush();
        }
    }

    @RequiredArgsConstructor
    static class Dao2 {

        @PersistenceContext(unitName = "unit2")
        private EntityManager em1;

        public List<SampleEntity2> listAll() {
            return em1.createQuery("select s from SampleEntity2 s", SampleEntity2.class).getResultList();
        }

        @Transactional("transactionManager2")
        public void insert() {
            em1.persist(new SampleEntity2());
            em1.flush();
        }
    }
}
