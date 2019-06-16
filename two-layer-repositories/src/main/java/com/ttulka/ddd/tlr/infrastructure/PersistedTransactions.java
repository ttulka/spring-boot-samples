package com.ttulka.ddd.tlr.infrastructure;

import java.util.stream.Stream;

import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.Transactions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedTransactions implements Transactions {

    private final TransactionEntries entries;

    @Override
    public Stream<Transaction> forAccount(String accountIban) {
        return entries.findBySenderIbanOrReceiverIban(accountIban, accountIban)
                .map(entry -> new PersistedTransaction(
                             entry.uuid, entry.amount, entry.currency, entry.senderIban, entry.receiverIban, entries
                     )
                );
    }
}
