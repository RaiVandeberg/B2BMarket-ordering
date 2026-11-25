package com.B2BMarket.ordering.domain.model.shoppingcart;
import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_NOT_FOUND;
import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_OF_CUSTOMER_NOT_FOUND;

public class ShoppingCartNotFoundException extends DomainException {

    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super(String.format(ERROR_SHOPPING_CART_NOT_FOUND, shoppingCartId));
    }

    public ShoppingCartNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_SHOPPING_CART_OF_CUSTOMER_NOT_FOUND, customerId));
    }
}
