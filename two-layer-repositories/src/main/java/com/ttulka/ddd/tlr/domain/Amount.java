package com.ttulka.ddd.tlr.domain;

import java.math.BigDecimal;

import com.ttulka.ddd.tlr.domain.ex.CurrenciesMishmashException;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class Amount {

    private final BigDecimal value;
    private final String currency;

    public BigDecimal value() {
        return value;
    }

    public Amount plus(Amount augend) {
        if (!currency.equals(augend.currency)) {
            throw new CurrenciesMishmashException();
        }
        return new Amount(value.add(augend.value), currency);
    }

    @Override
    public String toString() {
        return value + " " + currency;
    }
}
