package com.ttulka.ddd.tlr.infrastructure;

import java.util.Optional;

import com.ttulka.ddd.tlr.domain.Account;
import com.ttulka.ddd.tlr.domain.Accounts;
import com.ttulka.ddd.tlr.domain.Transactions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PersistedAccounts implements Accounts {

    private final AccountEntries entries;
    private final Transactions transactions;

    @Override
    public Optional<Account> byIban(String iban) {
        return entries.findById(iban).map(
                entry -> new PersistedAccount(
                        entry.iban, entry.currency, entry.ownerId, entries, transactions
                )
        );
    }
}
