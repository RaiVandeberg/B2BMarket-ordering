package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.commons.AddressTestDataBuilder;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingTest {

    @Test
    void given_validBilling_whenCreate_shouldSucceed() {
        Billing billing = Billing.builder()
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

        Assertions.assertThat(billing).isNotNull();
    }

    @Test
    void given_nullField_whenCreateBilling_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> Billing.builder()
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
