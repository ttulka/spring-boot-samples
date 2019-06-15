package com.ttulka.ddd.tlr.infrastructure;

import java.math.BigDecimal;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedTransaction implements Transaction {

    private final String uuid;
    private final String currency;
    private final BigDecimal amount;

    private final String accountIban;

    private final TransactionEntries entries;

    @Override
    public Amount amountFor(Account account) {
        if (!accountIban.equals(account.iban())) {
            throw new IllegalStateException("Not an account's transaction.");
        }
        return new Amount(amount, currency);
    }

    @Override
    public void book() {
        entries.save(new TransactionEntries.TransactionEntry(uuid, currency, amount, accountIban));
    }

    @Override
    public String toString() {
        return "[" + uuid + " | " + amount + " " + currency + "]";
    }
}
