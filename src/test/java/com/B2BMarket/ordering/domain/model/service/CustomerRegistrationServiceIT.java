package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.valueObject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class CustomerRegistrationServiceIT {

    @Autowired
    private CustomerRegistrationService customerRegistrationService;


    @Test
    public void shouldRegister(){

        var fullName = new FullName("John", "Doe");
        var birthDate = new BirthDate(LocalDate.of(1990, 1, 15));
        var email = new Email("john.doe@example.com");
        var phone = new Phone("11999990000");
        var document = new Document("12345678900");
        var address = Address.builder()
                .street("Rua das Flores")
                .number("100")
                .complement("Apto 42")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode(new ZipCode("01001"))
                .build();

        Customer customer = customerRegistrationService.register(
                fullName, birthDate, email, phone, document, true, address
        );

        Assertions.assertThat(customer.fullName()).isEqualTo(fullName);
        Assertions.assertThat(customer.birthDate()).isEqualTo(birthDate);
        Assertions.assertThat(customer.email()).isEqualTo(email);
        Assertions.assertThat(customer.phone()).isEqualTo(phone);
        Assertions.assertThat(customer.document()).isEqualTo(document);
        Assertions.assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
        Assertions.assertThat(customer.address()).isEqualTo(address);
        Assertions.assertThat(customer.id()).isNotNull();
        Assertions.assertThat(customer.isArchived()).isFalse();
        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
    }
}