package com.B2BMarket.ordering.infrastructure.persistence.embeddable;


import com.B2BMarket.ordering.domain.model.valueObject.*;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BillingEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    @Embedded
    private AddressEmbeddable address;
}
