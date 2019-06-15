package com.ttulka.ddd.tlr.infrastructure;

import java.math.BigDecimal;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.Transaction;
import com.ttulka.ddd.tlr.domain.ex.UnknownAccountException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedTransaction implements Transaction {

    private final String uuid;
    private final BigDecimal amount;

    private final String senderIban;
    private final String receiverIban;

    private final TransactionEntries entries;

    @Override
    public Amount amountFor(Account account) {
        if (!senderIban.equals(account.iban()) && !receiverIban.equals(account.iban())) {
            throw new UnknownAccountException();
        }
        return new Amount(
                receiverIban.equals(account.iban()) ? amount : amount.negate(),
                account.currency()
        );
    }

    @Override
    public void book() {
        entries.save(new TransactionEntries.TransactionEntry(
                uuid, amount, senderIban, receiverIban));
    }

    @Override
    public String toString() {
        return "[" + uuid + " | " + senderIban + " > " + receiverIban + " : " + amount + "]";
    }
}
