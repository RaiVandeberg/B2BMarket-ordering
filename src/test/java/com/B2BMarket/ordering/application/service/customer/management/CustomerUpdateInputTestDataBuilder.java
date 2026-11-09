package com.B2BMarket.ordering.application.service.customer.management;

import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.application.customer.management.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCustomerUpdate(){
        return CustomerUpdateInput.builder()
                .firstName("Jane")
                .lastName("Smith")
                .phone("478-256-2605")
                .promotionNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("Elm Street")
                        .number("2246")
                        .complement("Suite 220")
                        .neighborhood("West End")
                        .city("New Haven")
                        .state("North Carolina")
                        .zipCode("54321")
                        .build());
    }
}
