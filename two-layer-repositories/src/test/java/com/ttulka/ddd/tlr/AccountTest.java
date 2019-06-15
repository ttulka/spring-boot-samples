package com.ttulka.ddd.tlr;

import java.util.Optional;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.infrastructure.AccountEntries;
import com.ttulka.ddd.tlr.infrastructure.PersistedAccount;
import com.ttulka.ddd.tlr.infrastructure.PersistedAccounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Config.class)
@EnableAutoConfiguration
@Transactional
public class AccountTest {

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
    void openedAccount_shouldBeFoundByIban(@Autowired AccountEntries entries, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );
        account.open();

        Accounts accounts = new PersistedAccounts(entries, transactions);
        Optional<Account> persistentAccount = accounts.byIban("LT601010012345678901");

        assertTrue(persistentAccount.isPresent());
    }

    @Test
    void openedAccountAttributes_shouldBeSet(@Autowired AccountEntries entries, @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                entries,
                transactions
        );
        account.open();

        Accounts accounts = new PersistedAccounts(entries, transactions);
        Account persistentAccount = accounts.byIban("LT601010012345678901").get();

        assertAll(
                () -> assertEquals("LT601010012345678901", persistentAccount.iban()),
                () -> assertEquals("EUR", persistentAccount.currency()),
                () -> assertTrue(persistentAccount.isOwnedBy(000001))
        );
    }
}
