package com.B2BMarket.ordering.domain.model.customer;
import com.B2BMarket.ordering.domain.model.DomainException;


import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
    }
}
