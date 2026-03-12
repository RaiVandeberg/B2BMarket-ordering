package com.B2BMarket.ordering.domain.valueObject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record BillingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Email email,
        Address address
) {
    public BillingInfo {
        Objects.requireNonNull(fullName, "FullName is required");
        Objects.requireNonNull(document, "Document is required");
        Objects.requireNonNull(phone, "Phone is required");
        Objects.requireNonNull(email, "Email is required");
        Objects.requireNonNull(address, "Address is required");
    }
}
