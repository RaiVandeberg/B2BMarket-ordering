package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {

    public CustomerAlreadyHaveShoppingCartException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART, customerId));
    }
}
