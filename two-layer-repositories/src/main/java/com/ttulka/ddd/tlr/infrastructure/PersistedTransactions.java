package com.ttulka.ddd.tlr.infrastructure;

import java.util.stream.Stream;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedTransactions implements Transactions {

    private final TransactionEntries entries;

    @Override
    public Stream<Transaction> forAccount(Account account) {
        return entries.findByAccountIban(account.iban()).map(
                entry -> new PersistedTransaction(
                        entry.uuid, entry.currency, entry.amount, entry.accountIban, entries
                )
        );
    }
}
