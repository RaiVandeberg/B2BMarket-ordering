package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.commons.AddressTestDataBuilder;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingInfoTest {

    @Test
    void given_validBillingInfo_whenCreate_shouldSucceed() {
        BillingInfo billingInfo = BillingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("123-45-6789"))
                .phone(new Phone("123-456-7890"))
                .email(new Email("john.doe@email.com"))
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
