package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Billing(
        FullName fullName,
        Document document,
        Phone phone,
        Email email,
        Address address
) {
    public Billing {
        Objects.requireNonNull(fullName, "FullName is required");
        Objects.requireNonNull(document, "Document is required");
        Objects.requireNonNull(phone, "Phone is required");
        Objects.requireNonNull(email, "Email is required");
        Objects.requireNonNull(address, "Address is required");
    }
}
