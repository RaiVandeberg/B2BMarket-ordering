package com.B2BMarket.ordering.domain.valueObject;

import com.B2BMarket.ordering.domain.entity.CustomerTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingInfoTest {

    @Test
    void given_validBillingInfo_whenCreate_shouldSucceed() {
        BillingInfo billingInfo = BillingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("123-45-6789"))
                .phone(new Phone("123-456-7890"))
                .address(
                        AddressTestDataBuilder
                                .defaultAddress()
                                .build()
                )
                .build();

        Assertions.assertThat(billingInfo).isNotNull();
    }

    @Test
    void given_nullField_whenCreateBillingInfo_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BillingInfo.builder()
                        .fullName(null)
                        .document(new Document("123-45-6789"))
                        .phone(new Phone("123-456-7890"))
                        .address(
                                AddressTestDataBuilder
                                        .defaultAddress()
                                        .build()
                        )
                        .build());
    }
}
