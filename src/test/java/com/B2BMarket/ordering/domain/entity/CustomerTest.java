package com.B2BMarket.ordering.domain.entity;


import com.B2BMarket.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    new Customer(
                        IdGenerator.generateTimeBasedUUID(),
                        "Rai Braz",
                        LocalDate.of(1991, 7, 10),
                        "invalid",
                        "478-256-2504",
                        "255-08-0578",
                        false,
                        OffsetDateTime.now()
         );
       });
    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_shouldGenerateException() {
                    Customer customer = new Customer(
                            IdGenerator.generateTimeBasedUUID(),
                            "Rai Braz",
                            LocalDate.of(1991, 7, 10),
                            "railindo@gmail.com",
                            "478-256-2504",
                            "255-08-0578",
                            false,
                            OffsetDateTime.now()
                    );
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    customer.changeEmail("invalid");
                });


    }

}