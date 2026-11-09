package com.B2BMarket.ordering.application.checkout;

import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.domain.model.commons.*;
import com.B2BMarket.ordering.domain.model.order.BillingInfo;
import org.springframework.stereotype.Component;

@Component
class BillingInputDisassembler {

    public BillingInfo toDomainModel(BillingData billingData) {
        AddressData address = billingData.getAddress();
        return BillingInfo.builder()
                .fullName(new FullName(billingData.getFirstName(), billingData.getLastName()))
                .document(new Document(billingData.getDocument()))
                .phone(new Phone(billingData.getPhone()))
                .email(new Email(billingData.getEmail()))
                .address(Address.builder()
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .complement(address.getComplement())
                        .neighborhood(address.getNeighborhood())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build())
                .build();
    }
}