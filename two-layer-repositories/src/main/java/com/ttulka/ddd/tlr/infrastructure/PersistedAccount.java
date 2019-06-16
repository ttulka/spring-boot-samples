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
    public Amount balance() {
        return transactions.forAccount(iban)
                .map(transaction -> transaction.amountFor(iban))
                .reduce(Amount::plus)
                .orElse(new Amount(BigDecimal.ZERO, currency));
    }

    @Override
    public boolean isOwnedBy(int customerId) {
        return ownerId == customerId;
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
