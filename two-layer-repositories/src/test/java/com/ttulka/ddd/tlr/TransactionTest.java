package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.domain.ex.UnknownAccountException;
import com.ttulka.ddd.tlr.infrastructure.AccountEntries;
import com.ttulka.ddd.tlr.infrastructure.PersistedAccount;
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
public class TransactionTest {

    @Test
    void bookTransaction_shouldPersistEntity(@Autowired TransactionEntries entries) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "LT601010012345678901",
                "LT601010012345678902",
                entries
        );
        transaction.book();

        assertTrue(entries.existsById("00001"));
    }

    @Test
    void bookedTransaction_shouldBeFoundByAccounts(
            @Autowired TransactionEntries transactionEntries, @Autowired AccountEntries accountEntries,
            @Autowired Transactions transactions) {
        Account account1 = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        Account account2 = new PersistedAccount(
                "LT601010012345678902",
                "EUR",
                000001,
                accountEntries,
                transactions
        );

        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                account1.iban(),
                account2.iban(),
                transactionEntries
        );
        transaction.book();

        assertAll(
                () -> assertEquals(1, transactions.forAccount(account1).count()),
                () -> assertEquals(1, transactions.forAccount(account2).count())
        );
    }

    @Test
    void bookedTransactionAttributes_shouldBePersisted(
            @Autowired TransactionEntries transactionEntries, @Autowired AccountEntries accountEntries,
            @Autowired Transactions transactions) {
        Account account1 = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        Account account2 = new PersistedAccount(
                "LT601010012345678902",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                account1.iban(),
                account2.iban(),
                transactionEntries
        );
        transaction.book();

        Transaction persistentTransaction = transactions.forAccount(account1).findFirst().get();

        assertAll(
                () -> assertEquals(new Amount(BigDecimal.ONE.negate(), "EUR"), persistentTransaction.amountFor(account1)),
                () -> assertEquals(new Amount(BigDecimal.ONE, "EUR"), persistentTransaction.amountFor(account2))
        );
    }

    @Test
    void unknownAccountInTransaction_shouldThrowException(
            @Autowired TransactionEntries transactionEntries, @Autowired AccountEntries accountEntries,
            @Autowired Transactions transactions) {
        Transaction transaction = new PersistedTransaction(
                "00001",
                BigDecimal.ONE,
                "LT601010012345678901",
                "LT601010012345678902",
                transactionEntries
        );
        Account unknownAccount = new PersistedAccount(
                "LT000000000000000000",
                "EUR",
                000001,
                accountEntries,
                transactions
        );

        assertThrows(UnknownAccountException.class, () -> transaction.amountFor(unknownAccount));
    }
}
