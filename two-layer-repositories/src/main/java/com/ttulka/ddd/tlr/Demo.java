package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.infrastructure.AccountEntries;
import com.ttulka.ddd.tlr.infrastructure.PersistedAccount;
import com.ttulka.ddd.tlr.infrastructure.PersistedTransaction;
import com.ttulka.ddd.tlr.infrastructure.TransactionEntries;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Demo {

    private final Accounts accounts;
    private final Transactions transactions;
    private final AccountEntries accountEntries;
    private final TransactionEntries transactionEntries;

    @Transactional
    public void run() {
        // open a couple of accounts
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

        // book a couple of transactions
        Transaction transaction1 = new PersistedTransaction(
                "00001",
                new BigDecimal(1000),
                account1.iban(),
                account2.iban(),
                transactionEntries
        );
        transaction1.book();

        Transaction transaction2 = new PersistedTransaction(
                "00002",
                new BigDecimal(100),
                account1.iban(),
                account2.iban(),
                transactionEntries
        );
        transaction2.book();

        Transaction transaction3 = new PersistedTransaction(
                "00003",
                new BigDecimal(200),
                account2.iban(),
                account1.iban(),
                transactionEntries
        );
        transaction3.book();

        log.info("================================");
        log.info("");

        // fetch accounts
        accounts.byIban("LT601010012345678901").ifPresent(a -> {
            log.info("Account details:");
            log.info("--------------------------------");
            log.info(a.toString());
            log.info("Balance: " + a.balance());
            log.info("");
        });
        accounts.byIban("LT601010012345678902").ifPresent(a -> {
            log.info("Account details:");
            log.info("--------------------------------");
            log.info(a.toString());
            log.info("Balance: " + a.balance());
            log.info("");
        });

        log.info("================================");
        log.info("");

        // fetch transactions for the account
        transactions.forAccount(account1).forEach(t -> {
            log.info("Transaction found:");
            log.info("--------------------------------");
            log.info(t.toString());
            log.info("");
        });
    }
}
