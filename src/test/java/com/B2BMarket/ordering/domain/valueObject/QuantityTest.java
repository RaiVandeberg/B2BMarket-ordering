package com.B2BMarket.ordering.domain.valueObject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuantityTest {

    @Test
    void given_validQuantity_whenCreate_shouldSucceed() {
        Quantity quantity = new Quantity(5);

        assertThat(quantity.value()).isEqualTo(5);
    }

    @Test
    void given_negativeQuantity_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-1));
    }

    @Test
    void given_twoQuantities_whenAdd_shouldSumCorrectly() {
        Quantity q1 = new Quantity(2);
        Quantity q2 = new Quantity(3);

        Quantity result = q1.add(q2);

        assertThat(result.value()).isEqualTo(5);
    }

    @Test
    void given_twoQuantities_whenCompare_shouldWorkCorrectly() {
        Quantity q1 = new Quantity(1);
        Quantity q2 = new Quantity(2);

        assertThat(q1.compareTo(q2)).isLessThan(0);
        assertThat(q2.compareTo(q1)).isGreaterThan(0);
        assertThat(q1.compareTo(new Quantity(1))).isZero();
    }
}
