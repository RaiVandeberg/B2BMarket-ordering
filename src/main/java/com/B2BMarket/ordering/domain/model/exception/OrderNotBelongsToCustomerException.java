package com.B2BMarket.ordering.domain.model.exception;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_ORDER_NOT_BELONGS_TO_CUSTOMER;

public class OrderNotBelongsToCustomerException extends DomainException {

    public OrderNotBelongsToCustomerException() {
        super(ERROR_ORDER_NOT_BELONGS_TO_CUSTOMER);
    }
}
