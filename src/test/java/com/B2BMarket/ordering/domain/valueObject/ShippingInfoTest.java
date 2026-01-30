package com.B2BMarket.ordering.domain.valueObject;

import com.B2BMarket.ordering.domain.entity.CustomerTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShippingInfoTest {

    @Test
    void given_validShippingInfo_whenCreate_shouldSucceed() {
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .fullName(new FullName("Jane", "Doe"))
                .document(new Document("987-65-4321"))
                .phone(new Phone("999-888-7777"))
                .address(
                        AddressTestDataBuilder
                                .defaultAddress()
                                .build()
                )
                .build();

        Assertions.assertThat(shippingInfo).isNotNull();
    }

    @Test
    void given_nullField_whenCreateShippingInfo_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ShippingInfo.builder()
                        .fullName(new FullName("Jane", "Doe"))
                        .document(null)
                        .phone(new Phone("999-888-7777"))
                        .address(
                                AddressTestDataBuilder
                                        .defaultAddress()
                                        .build()
                        )
                        .build());
    }
}
