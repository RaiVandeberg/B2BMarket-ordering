package com.B2BMarket.ordering.domain.model.customer;

import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException( Throwable cause) {
        super(ERROR_CUSTOMER_ARCHIVED, cause);
    }

    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}
