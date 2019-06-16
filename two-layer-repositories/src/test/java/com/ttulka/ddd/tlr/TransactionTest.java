package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.domain.ex.UnknownAccountException;
import com.ttulka.ddd.tlr.infrastructure.PersistedTransaction;
import com.ttulka.ddd.tlr.infrastructure.TransactionEntries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Config.class)
@EnableAutoConfiguration
@Transactional
class TransactionTest {

    @Test
    void bookTransaction_shouldPersistEntity(@Autowired TransactionEntries entries) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "EUR",
                "LT601010012345678901",
                "LT601010012345678902",
                entries
        );
        transaction.book();

        assertTrue(entries.existsById("00001"));
    }

    @Test
    void bookedTransaction_shouldBeFoundByAccounts(
            @Autowired TransactionEntries transactionEntries, @Autowired Transactions transactions) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "EUR",
                "LT601010012345678901",
                "LT601010012345678902",
                transactionEntries
        );
        transaction.book();

        assertAll(
                () -> assertEquals(1, transactions.forAccount("LT601010012345678901").count()),
                () -> assertEquals(1, transactions.forAccount("LT601010012345678902").count())
        );
    }

    @Test
    void bookedTransactionAttributes_shouldBePersisted(
            @Autowired TransactionEntries transactionEntries, @Autowired Transactions transactions) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "EUR",
                "LT601010012345678901",
                "LT601010012345678902",
                transactionEntries
        );
        transaction.book();

        Transaction persistentTransaction = transactions.forAccount("LT601010012345678901").findFirst().get();

        assertAll(
                () -> assertEquals(new Amount(BigDecimal.ONE.negate(), "EUR"),
                                   persistentTransaction.amountFor("LT601010012345678901")),
                () -> assertEquals(new Amount(BigDecimal.ONE, "EUR"),
                                   persistentTransaction.amountFor("LT601010012345678902"))
        );
    }

    @Test
    void unknownAccountInTransaction_shouldThrowException(@Autowired TransactionEntries transactionEntries) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "EUR",
                "LT601010012345678901",
                "LT601010012345678902",
                transactionEntries
        );

        assertThrows(UnknownAccountException.class, () -> transaction.amountFor("LT000000000000000000"));
    }
}
