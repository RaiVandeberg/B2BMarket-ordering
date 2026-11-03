package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
    }
}
