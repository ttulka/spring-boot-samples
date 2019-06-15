package com.ttulka.ddd.tlr.domain;

public interface Account {

    String iban();

    String currency();

    Amount balance();

    boolean isOwnedBy(int customer);

    void open();
}
