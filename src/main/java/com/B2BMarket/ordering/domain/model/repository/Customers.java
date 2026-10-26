package com.B2BMarket.ordering.domain.model.repository;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.valueObject.Email;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
    boolean isEmailUnique(Email email, CustomerId exceptCustomerId);
}
