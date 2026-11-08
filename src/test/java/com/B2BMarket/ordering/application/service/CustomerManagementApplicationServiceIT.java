package com.B2BMarket.ordering.application.service;


import com.B2BMarket.ordering.application.model.AddressData;
import com.B2BMarket.ordering.application.model.CustomerInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;


    @Test
    public void shouldRegister(){
        CustomerInput input = CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe" + UUID.randomUUID() + "@email.com")
                .phone("478-256-2604")
                .document("255-08-0578")
                .birthDate(LocalDate.of(1991, 7, 5))
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .Complement("Apt. 114")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .build())
                .build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();
    }

}