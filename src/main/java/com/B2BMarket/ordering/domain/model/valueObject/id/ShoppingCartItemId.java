package com.B2BMarket.ordering.domain.model.valueObject.id;

import com.B2BMarket.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ShoppingCartItemId(TSID value) {

    public ShoppingCartItemId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTSID());
    }

    public ShoppingCartItemId(Long value) {
        this(TSID.from(value));
    }

    public ShoppingCartItemId(String value) {
        this(TSID.from(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
