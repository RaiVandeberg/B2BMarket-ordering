package com.B2BMarket.ordering.domain.valueObject;

import java.util.Objects;

public record Quantity(int value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(other, "Quantity to add cannot be null");
        return new Quantity(this.value + other.value);
    }

    @Override
    public int compareTo(Quantity other) {
        Objects.requireNonNull(other, "Quantity to compare cannot be null");
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

