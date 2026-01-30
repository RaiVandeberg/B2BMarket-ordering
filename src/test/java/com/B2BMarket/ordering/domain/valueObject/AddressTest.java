package com.B2BMarket.ordering.domain.valueObject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void given_validAddress_whenCreate_shouldSucceed() {
        Address address = AddressTestDataBuilder
                .defaultAddress()
                .build();

        Assertions.assertThat(address).isNotNull();
    }

    @Test
    void given_blankStreet_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AddressTestDataBuilder
                        .defaultAddress()
                        .street("   ")
                        .build());
    }

    @Test
    void given_nullNeighborhood_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder
                        .defaultAddress()
                        .neighborhood(null)
                        .build());
    }

    @Test
    void given_nullZipCode_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> AddressTestDataBuilder
                        .defaultAddress()
                        .zipCode(null)
                        .build());
    }

    @Test
    void given_nullComplement_whenCreate_shouldSucceed() {
        Address address = AddressTestDataBuilder
                .defaultAddress()
                .complement(null)
                .build();

        Assertions.assertThat(address.complement()).isNull();
    }
}
