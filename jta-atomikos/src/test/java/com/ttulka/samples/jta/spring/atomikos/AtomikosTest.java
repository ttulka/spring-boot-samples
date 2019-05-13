package com.ttulka.samples.jta.spring.atomikos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.TransactionManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ttulka.samples.jta.spring.atomikos.model1.SampleEntity1;
import com.ttulka.samples.jta.spring.atomikos.model2.SampleEntity2;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {AtomikosTest.Dao1.class, AtomikosTest.Dao2.class, PersistenceExceptionTranslationPostProcessor.class})
@Transactional(readOnly=false,propagation= Propagation.REQUIRES_NEW)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class AtomikosTest {

    AtomikosTest(Dao1 dao1, Dao2 dao2, TransactionManager transactionManager) {
        this.dao1 = dao1;
        this.dao2 = dao2;
        this.transactionManager = transactionManager;
    }

    private Dao1 dao1;
    private Dao2 dao2;
    private TransactionManager transactionManager;

    @Test
    @SneakyThrows
    public void save_with_two_separate_DAOs_must_work() {
        dao1.insert();

        dao2.insert();
        dao2.insert();

        assertThat(dao1.listAll()).hasSize(1);
        assertThat(dao2.listAll()).hasSize(2);
    }

    @Test
    @SneakyThrows
    public void exception_in_one_DAO_rollback_the_entire_transaction() {
        assertThat(dao1.listAll()).hasSize(0);

        // Exception occurs and marks the actuall transaction as rollback
        Assertions.assertThrows(ForcedException.class, () -> {
            dao1.forceException();
        });

        assertThat(transactionManager.getStatus()).isEqualTo(Status.STATUS_MARKED_ROLLBACK);
        Assertions.assertThrows(javax.persistence.PersistenceException.class, () -> {
            dao2.listAll();
        });
    }

    @RequiredArgsConstructor
    static class Dao1 {

        @PersistenceContext(unitName = "unit1")
        private EntityManager em1;

        public List<SampleEntity1> listAll() {
            return em1.createQuery("select s from SampleEntity1 s", SampleEntity1.class).getResultList();
        }

        @Transactional
        public void insert() {
            em1.persist(new SampleEntity1());
            em1.flush();
        }

        @Transactional
        public void forceException() {
            throw new ForcedException();
        }
    }

    @RequiredArgsConstructor
    static class Dao2 {

        @PersistenceContext(unitName = "unit2")
        protected EntityManager em2;

        public List<SampleEntity2> listAll() {
            return em2.createQuery("select s from SampleEntity2 s", SampleEntity2.class).getResultList();
        }

        @Transactional
        public void insert() {
            em2.persist(new SampleEntity2());
            em2.flush();
        }
    }

    private static class ForcedException extends RuntimeException {
    }
}
