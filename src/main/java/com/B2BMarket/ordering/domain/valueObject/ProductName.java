package com.B2BMarket.ordering.domain.valueObject;

import java.util.Objects;

public record ProductName(String value) {

    public ProductName {
        Objects.requireNonNull(value, "Product name cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
