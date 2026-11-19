package com.B2BMarket.ordering.infrastructure.persistence.customer;

import com.B2BMarket.ordering.application.customer.query.CustomerOutput;

import java.util.Optional;
import java.util.UUID;

public interface CustomerPersistenceEntityQueries {
    Optional<CustomerOutput> findByAsOutput(UUID customerId);
}
