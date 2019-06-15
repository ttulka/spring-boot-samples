package com.ttulka.ddd.tlr;

import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Transactions;
import com.ttulka.ddd.tlr.infrastructure.AccountEntries;
import com.ttulka.ddd.tlr.infrastructure.PersistedAccounts;
import com.ttulka.ddd.tlr.infrastructure.PersistedTransactions;
import com.ttulka.ddd.tlr.infrastructure.TransactionEntries;

import org.springframework.context.annotation.Bean;

public class Config {

    @Bean
    public Transactions transactions(TransactionEntries entries) {
        return new PersistedTransactions(entries);
    }

    @Bean
    public Accounts accounts(AccountEntries entries, Transactions transactions) {
        return new PersistedAccounts(entries, transactions);
    }
}
