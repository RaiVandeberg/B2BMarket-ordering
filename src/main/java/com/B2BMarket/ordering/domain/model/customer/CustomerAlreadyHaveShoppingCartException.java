package com.B2BMarket.ordering.domain.model.customer;
import com.B2BMarket.ordering.domain.model.DomainException;


import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {

    public CustomerAlreadyHaveShoppingCartException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART, customerId));
    }
}
