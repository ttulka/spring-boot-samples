package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Account;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Config.class)
@EnableAutoConfiguration
@Transactional
public class AccountTransactionsTest {

    @Test
    void accountBalance_shouldBeCalculatedFromTransactions(
            @Autowired AccountEntries accountEntries,
            @Autowired TransactionEntries transactionEntries,
            @Autowired Transactions transactions) {
        Account account = new PersistedAccount(
                "LT601010012345678901",
                "EUR",
                000001,
                accountEntries,
                transactions
        );
        account.open();

        Transaction transaction1 = new PersistedTransaction(
                "00001",
                "EUR",
                BigDecimal.TEN,
                "LT601010012345678901",
                transactionEntries
        );
        transaction1.book();
        Transaction transaction2 = new PersistedTransaction(
                "00002",
                "EUR",
                BigDecimal.TEN,
                "LT601010012345678901",
                transactionEntries
        );
        transaction2.book();

        assertEquals(new Amount(new BigDecimal(20), "EUR"), account.balance());
    }
}
