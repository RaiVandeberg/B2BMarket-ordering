package com.B2BMarket.ordering.domain.valueObject;

public class AddressTestDataBuilder {

    private AddressTestDataBuilder() {
    }

    public static Address.AddressBuilder defaultAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement("Apt. 114");
    }
}