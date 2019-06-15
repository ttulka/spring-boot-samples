package com.ttulka.ddd.tlr.domain;

import java.util.stream.Stream;

public interface Transactions {

    Stream<Transaction> forAccount(Account account);
}
