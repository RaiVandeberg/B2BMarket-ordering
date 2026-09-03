package com.B2BMarket.ordering.domain.model.valueObject.id;

import com.B2BMarket.ordering.domain.model.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        Objects.requireNonNull(value);
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
