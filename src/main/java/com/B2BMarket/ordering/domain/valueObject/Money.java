package com.B2BMarket.ordering.domain.valueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(value, "Money value cannot be null");

        value = value.setScale(2, ROUNDING_MODE);

        if (value.signum() < 0) {
            throw new IllegalArgumentException("Money value cannot be negative");
        }
    }

    public Money(String value) {
        this(new BigDecimal(
                Objects.requireNonNull(value, "Money value cannot be null")
        ));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        if (quantity.value() < 1) {
            throw new IllegalArgumentException("Quantity must be >= 1 to multiply Money");
        }

        return new Money(this.value.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Money to add cannot be null");
        return new Money(this.value.add(other.value));
    }

    public Money divide(Money other) {
        Objects.requireNonNull(other, "Money to divide cannot be null");

        if (other.value.signum() == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        return new Money(this.value.divide(other.value, 2, ROUNDING_MODE));
    }

    @Override
    public int compareTo(Money other) {
        Objects.requireNonNull(other, "Money to compare cannot be null");
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
