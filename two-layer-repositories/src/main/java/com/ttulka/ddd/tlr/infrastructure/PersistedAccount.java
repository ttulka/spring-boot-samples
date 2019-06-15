package com.ttulka.ddd.tlr.infrastructure;

import java.math.BigDecimal;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transactions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedAccount implements Account {

    private final String iban;
    private final String currency;
    private final int ownerId;

    private final AccountEntries entries;
    private final Transactions transactions;

    @Override
    public String iban() {
        return iban;
    }

    @Override
    public String currency() {
        return currency;
    }

    @Override
    public Amount balance() {
        return transactions.forAccount(this)
                .map(transaction -> transaction.amountFor(this))
                .reduce(Amount::plus)
                .orElse(new Amount(BigDecimal.ZERO, "EUR"));
    }

    @Override
    public boolean isOwnedBy(int customer) {
        return ownerId == customer;
    }

    @Override
    public void open() {
        entries.save(new AccountEntries.AccountEntry(iban, currency, ownerId));
    }

    @Override
    public String toString() {
        return "[" + iban + " | " + currency + "]";
    }
}
