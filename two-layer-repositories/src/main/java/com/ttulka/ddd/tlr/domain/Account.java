package com.ttulka.ddd.tlr.domain;

public interface Account {

    Amount balance();

    boolean isOwnedBy(int customerId);

    void open();
}
