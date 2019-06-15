package com.ttulka.ddd.tlr.domain;

public interface Transaction {

    Amount amountFor(Account account);

    void book();
}
