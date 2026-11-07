package com.B2BMarket.ordering.domain.model.customer;

import com.B2BMarket.ordering.domain.model.Repository;
import com.B2BMarket.ordering.domain.model.commons.Email;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
    boolean isEmailUnique(Email email, CustomerId exceptCustomerId);
}
