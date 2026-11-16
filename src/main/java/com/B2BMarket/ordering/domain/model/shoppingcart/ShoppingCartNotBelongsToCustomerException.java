package com.B2BMarket.ordering.domain.model.shoppingcart;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_NOT_BELONGS_TO_CUSTOMER;

public class ShoppingCartNotBelongsToCustomerException extends DomainException {

    public ShoppingCartNotBelongsToCustomerException() {
        super(ERROR_SHOPPING_CART_NOT_BELONGS_TO_CUSTOMER);
    }
}
