package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.exception.CustomerArchivedException;
import com.B2BMarket.ordering.domain.valueObject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class CustomerTest {

    private Customer buildCustomer() {
        return new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                new Email("john.doe@gmail.com"),
                new Phone("478-256-2504"),
                new Document("255-08-0578"),
                false,
                OffsetDateTime.now()
        );
    }

    @Test
    void given_invalidEmail_whenCreateCustomer_shouldThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->
                        new Customer(
                                new CustomerId(),
                                new FullName("John", "Doe"),
                                new BirthDate(LocalDate.of(1991, 7, 5)),
                                new Email("invalid"),
                                new Phone("478-256-2504"),
                                new Document("255-08-0578"),
                                false,
                                OffsetDateTime.now()
                        )
                );
    }


    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymizeCustomer() {
        Customer customer = buildCustomer();

        customer.archive();

        assertAll(
                () -> assertThat(customer.isArchived()).isTrue(),
                () -> assertThat(customer.fullName())
                        .isEqualTo(new FullName("Anonymous", "Anonymous")),
                () -> assertThat(customer.birthDate()).isNull(),
                () -> assertThat(customer.isPromotionNotificationsAllowed()).isFalse(),
                () -> assertThat(customer.email().value()).contains("@anonymous.com"),
                () -> assertThat(customer.phone())
                        .isEqualTo(new Phone("000-000-0000")),
                () -> assertThat(customer.document())
                        .isEqualTo(new Document("000-00-0000")),
                () -> assertThat(customer.archivedAt()).isNotNull()
        );
    }


    @Test
    void given_archivedCustomer_whenTryToChangeAnything_shouldThrowException() {
        Customer customer = buildCustomer();
        customer.archive();

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("new@email.com")));

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("123-123-1111")));

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(10)));
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = buildCustomer();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_invalidLoyaltyPoints_whenAdd_shouldThrowException() {
        Customer customer = buildCustomer();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }
}