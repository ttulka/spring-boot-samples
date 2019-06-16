package com.ttulka.ddd.tlr;

import java.math.BigDecimal;

import com.ttulka.ddd.tlr.domain.Amount;
import com.ttulka.ddd.tlr.domain.ex.CurrencyMishmashException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    void plusAmount_shouldReturnSum() {
        Amount amount1 = new Amount(BigDecimal.ONE, "EUR");
        Amount amount2 = new Amount(BigDecimal.ONE, "EUR");

        assertEquals(new Amount(new BigDecimal(2), "EUR"), amount1.plus(amount2));
    }

    @Test
    void plusAmountDifferentCurrencies_shouldThrowCurrencyMishmashException() {
        Amount amount1 = new Amount(BigDecimal.ONE, "EUR");
        Amount amount2 = new Amount(BigDecimal.ONE, "USD");

        assertThrows(CurrencyMishmashException.class, () -> amount1.plus(amount2));
    }
}
