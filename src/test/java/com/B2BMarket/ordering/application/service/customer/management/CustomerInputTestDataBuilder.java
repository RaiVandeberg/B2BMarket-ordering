package com.B2BMarket.ordering.application.service.customer.management;

import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInputTestDataBuilder {

    public static CustomerInput.CustomerInputBuilder aCustomer() {
        return CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@email.com")
                .phone("478-256-2604")
                .document("255-08-0578")
                .birthDate(LocalDate.of(1991, 7, 5))
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .complement("Apt. 114")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .build());

    }
}
