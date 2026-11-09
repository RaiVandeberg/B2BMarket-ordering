package com.B2BMarket.ordering.application.customer.management;

import com.B2BMarket.ordering.application.commons.AddressData;

import com.B2BMarket.ordering.application.utility.Mapper;
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

    private final Mapper mapper;

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
                toAddress(address)
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

        return mapper.convert(customer, CustomerOutput.class);
    }

    @Transactional
    public void update(UUID rawCustomerId, CustomerUpdateInput input){
        Objects.requireNonNull(input);
        Objects.requireNonNull(rawCustomerId);
        CustomerId id = new CustomerId(rawCustomerId);
        Customer customer = customers.ofId(new CustomerId(rawCustomerId))
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));

        if(Boolean.TRUE.equals(input.getPromotionNotificationsAllowed())){
            customer.enablePromotionNotifications();
        } else {
            customer.disablePromotionNotifications();
        }

        AddressData address = input.getAddress();

        customer.changeAdress(toAddress(address));

        customers.add(customer);
    }

    @Transactional
    public void archive(UUID rawCustomerId){
        Objects.requireNonNull(rawCustomerId);
        CustomerId id = new CustomerId(rawCustomerId);
        Customer customer = customers.ofId(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.archive();

        customers.add(customer);
    }

    @Transactional
    public void changeEmail(UUID rawCustomerId, String newEmail){
        Objects.requireNonNull(rawCustomerId);
        Objects.requireNonNull(newEmail);
        CustomerId id = new CustomerId(rawCustomerId);
        Customer customer = customers.ofId(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerRegistrationService.changeEmail(customer, new Email(newEmail));

        customers.add(customer);
    }

    private Address toAddress(AddressData address) {
        return Address.builder()
                .zipCode(new ZipCode(address.getZipCode()))
                .city(address.getCity())
                .state(address.getState())
                .neighborhood(address.getNeighborhood())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .build();
    }

}
