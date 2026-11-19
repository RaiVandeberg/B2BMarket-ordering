package com.B2BMarket.ordering.infrastructure.persistence.customer;

import com.B2BMarket.ordering.application.customer.query.CustomerOutput;
import com.B2BMarket.ordering.application.customer.query.CustomerQueryService;
import com.B2BMarket.ordering.application.utility.Mapper;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerPersistenceEntityRepository repository;


    @Override
    public CustomerOutput findById(UUID customerId) {
        Objects.requireNonNull(customerId);

        return repository.findByAsOutput(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(new CustomerId(customerId)));
    }
}
