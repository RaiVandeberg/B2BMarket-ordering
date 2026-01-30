package com.B2BMarket.ordering.domain.valueObject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {

    @Test
    void given_validValue_whenCreateMoney_shouldNormalizeScale() {
        Money money = new Money(new BigDecimal("10"));

        assertThat(money.value()).isEqualByComparingTo("10.00");
    }

    @Test
    void given_negativeValue_whenCreateMoney_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(new BigDecimal("-1")));
    }

    @Test
    void given_nullValue_whenCreateMoney_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Money((BigDecimal) null));
    }

    @Test
    void given_twoMoneyValues_whenAdd_shouldSumCorrectly() {
        Money m1 = new Money("10.50");
        Money m2 = new Money("2.25");

        Money result = m1.add(m2);

        assertThat(result.value()).isEqualByComparingTo("12.75");
    }

    @Test
    void given_moneyAndQuantity_whenMultiply_shouldReturnNewMoney() {
        Money money = new Money("10.00");
        Quantity quantity = new Quantity(3);

        Money result = money.multiply(quantity);

        assertThat(result.value()).isEqualByComparingTo("30.00");
    }

    @Test
    void given_quantityLessThanOne_whenMultiply_shouldThrowException() {
        Money money = new Money("10.00");

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> money.multiply(new Quantity(0)));
    }

    @Test
    void given_twoMoneyValues_whenDivide_shouldKeepHalfEven() {
        Money money = new Money("10.00");
        Money divisor = new Money("3.00");

        Money result = money.divide(divisor);

        assertThat(result.value()).isEqualByComparingTo("3.33");
    }

    @Test
    void given_twoMoneyValues_whenCompare_shouldWorkCorrectly() {
        Money m1 = new Money("5.00");
        Money m2 = new Money("10.00");

        assertThat(m1.compareTo(m2)).isLessThan(0);
        assertThat(m2.compareTo(m1)).isGreaterThan(0);
        assertThat(m1.compareTo(new Money("5.00"))).isZero();
    }
}
