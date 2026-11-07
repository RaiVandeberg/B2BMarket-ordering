package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_ORDER_NOT_BELONGS_TO_CUSTOMER;

public class OrderNotBelongsToCustomerException extends DomainException {

    public OrderNotBelongsToCustomerException() {
        super(ERROR_ORDER_NOT_BELONGS_TO_CUSTOMER);
    }
}
