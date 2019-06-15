package com.ttulka.ddd.tlr.domain;

import java.util.Optional;

public interface Accounts {

    Optional<Account> byIban(String iban);
}
