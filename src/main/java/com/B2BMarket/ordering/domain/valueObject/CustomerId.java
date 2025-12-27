package com.B2BMarket.ordering.domain.valueObject;

import com.B2BMarket.ordering.domain.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public CustomerId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
