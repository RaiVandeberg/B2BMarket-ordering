package com.B2BMarket.ordering.domain.entity;


import com.B2BMarket.ordering.domain.exception.CustomerArchivedException;
import com.B2BMarket.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Test
    void given_unarchiverCustomer_whenArchive_shouldAnonymize(){
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

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo("Anonymous"),
                c -> assertThat(c.email()).isNotEqualTo("railindo@gmail.com"),
                c -> assertThat(c.phone()).isEqualTo("000-000-0000"),
                c -> assertThat(c.document()).isEqualTo("000-00-0000"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse()
        );


    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Anonymous",
                null,
                "anonymous@anonymous.com",
                "000-000-000",
                "000-00-0000",
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                10

        );
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("email@email.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-123-1111"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.enablePromotionNotifications());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.disablePromotionNotifications());
    }


    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldAnonymize(){
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

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(30);


    }


    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
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
                .isThrownBy(() -> customer.addLoyaltyPoints(-1));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(0));


    }

}