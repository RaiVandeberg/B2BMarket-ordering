package com.B2BMarket.ordering.application.service;

import com.B2BMarket.ordering.application.model.AddressData;
import com.B2BMarket.ordering.application.model.CustomerInput;
import com.B2BMarket.ordering.application.model.CustomerOutput;
import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.customer.*;
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

    @Transactional(readOnly = true)
    public CustomerOutput findById(UUID customerId){
        Objects.requireNonNull(customerId);
        CustomerId id = new CustomerId(customerId);
        Customer customer = customers.ofId(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return CustomerOutput.builder()
                .id(customer.id().value())
                .firstName(customer.fullName().firstName())
                .lastName(customer.fullName().lastName())
                .email(customer.email().value())
                .document(customer.document().value())
                .phone(customer.phone().value())
                .birthDate(customer.birthDate() == null ? null : customer.birthDate().birthDate())
                .loyaltyPoints(customer.loyaltyPoints().value())
                .registeredAt(customer.registeredAt())
                .archivedAt(customer.archivedAt())
                .address(AddressData.builder()
                        .street(customer.address().street())
                        .number(customer.address().number())
                        .Complement(customer.address().complement())
                        .neighborhood(customer.address().neighborhood())
                        .city(customer.address().city())
                        .state(customer.address().state())
                        .zipCode(customer.address().zipCode().value())
                        .build())
                .promotionNotificationsAllowed(customer.isPromotionNotificationsAllowed())
                .archived(customer.isArchived())
                .build();
    }
}
