package com.ttulka.ddd.tlr;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Config.class)
@EnableAutoConfiguration
@Transactional
class AccountTest {

    @Test
    void openAccount_shouldPersistEntity(@Autowired AccountEntries entries, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );
        account.open();

        assertTrue(entries.existsById("LT601010012345678901"));
    }

    @Test
    void openedAccount_shouldBeFoundByIban(
            @Autowired AccountEntries entries,
            @Autowired Accounts accounts, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );
        account.open();

        Optional<Account> persistentAccount = accounts.byIban("LT601010012345678901");

        assertTrue(persistentAccount.isPresent());
    }

    @Test
    void accountBalance_shouldBeCalculatedFromTransactions(
            @Autowired AccountEntries accountEntries,
            @Autowired TransactionEntries transactionEntries,
            @Autowired Transactions transactions) {
        // open accounts
        Account account1 = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        account1.open();

        Account account2 = new PersistedAccount(
                "LT601010012345678902",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        account2.open();

        // book transactions
        Transaction transaction1 = new PersistedTransaction(
                "00001",
                new BigDecimal(100),
                "EUR",
                "LT601010012345678901",
                "LT601010012345678902",
                transactionEntries
        );
        transaction1.book();

        Transaction transaction2 = new PersistedTransaction(
                "00002",
                new BigDecimal(1),
                "EUR",
                "LT601010012345678902",
                "LT601010012345678901",
                transactionEntries
        );
        transaction2.book();

        assertAll(
                () -> assertEquals(new Amount(new BigDecimal(-99), "EUR"), account1.balance()),
                () -> assertEquals(new Amount(new BigDecimal(99), "EUR"), account2.balance())
        );
    }

    @Test
    void isOwned_shouldReturnTrueForAccountOwner(@Autowired AccountEntries entries, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );

        assertTrue(account.isOwnedBy(000001));
    }

    @Test
    void isOwned_shouldReturnFalseForStranger(@Autowired AccountEntries entries, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );

        assertFalse(account.isOwnedBy(999999));
    }
}
