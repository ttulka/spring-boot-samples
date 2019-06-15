package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.infrastructure.PersistedTransaction;
import com.ttulka.ddd.tlr.infrastructure.TransactionEntries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Config.class)
@EnableAutoConfiguration
@Transactional
public class TransactionTest {

    @Test
    void bookTransaction_shouldPersistEntity(@Autowired TransactionEntries entries) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                "EUR",
                BigDecimal.ONE,
                "LT601010012345678901",
                entries
        );
        transaction.book();

        assertTrue(entries.existsById("00001"));
    }
}
