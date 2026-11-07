package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.Money;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder(toBuilder = true)
public record Shipping(
        Money cost,
        LocalDate expectedDate,
        Recipient recipient,
        Address address
) {
    public Shipping {
        Objects.requireNonNull(expectedDate);
        Objects.requireNonNull(cost);
        Objects.requireNonNull(recipient);
        Objects.requireNonNull(address);
    }
}
