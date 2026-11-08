package com.B2BMarket.ordering.application.service;

import com.B2BMarket.ordering.application.model.AddressData;
import com.B2BMarket.ordering.application.model.CustomerInput;
import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.customer.BirthDate;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerRegistrationService;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final CustomerRegistrationService customerRegistrationService;
    private final Customers customers;

    @Transactional
    public UUID create(CustomerInput input){
        Objects.requireNonNull(input);

        AddressData address= input.getAddress();

        Customer customer = customerRegistrationService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new BirthDate(input.getBirthDate()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                new Document(input.getDocument()),
                input.getPromotionNotificationsAllowed(),
                Address.builder()
                       .zipCode(new ZipCode(address.getZipCode()))
                       .city(address.getCity())
                       .state(address.getState())
                       .neighborhood(address.getNeighborhood())
                       .street(address.getStreet())
                       .number(address.getNumber())
                       .complement(address.getComplement())
                       .build()
        );

        customers.add(customer);

        return customer.id().value();
    }
}
