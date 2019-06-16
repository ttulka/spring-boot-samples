package com.ttulka.ddd.tlr.domain;

public interface Transaction {

    Amount amountFor(String accountIban);

    void book();
}
