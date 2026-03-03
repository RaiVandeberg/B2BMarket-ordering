package com.B2BMarket.ordering.domain.valueObject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ShippingInfoTest {

    @Test
    void given_validShippingInfo_whenCreate_shouldSucceed() {

        Recipient recipient = Recipient.builder()
                .fullName(new FullName("Jane", "Doe"))
                .document(new Document("987-65-4321"))
                .phone(new Phone("999-888-7777"))
                .build();

        Shipping shipping = Shipping.builder()
                .cost(new Money("50"))
                .expectedDate(LocalDate.now().plusDays(3))
                .recipient(recipient)
                .address(
                        AddressTestDataBuilder
                                .defaultAddress()
                                .build()
                )
                .build();

        Assertions.assertThat(shipping).isNotNull();
    }

    @Test
    void given_nullField_whenCreateShippingInfo_shouldThrowException() {

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> Shipping.builder()
                        .cost(new Money("50"))
                        .expectedDate(LocalDate.now().plusDays(3))
                        .recipient(null)
                        .address(
                                AddressTestDataBuilder
                                        .defaultAddress()
                                        .build()
                        )
                        .build());
    }
}
