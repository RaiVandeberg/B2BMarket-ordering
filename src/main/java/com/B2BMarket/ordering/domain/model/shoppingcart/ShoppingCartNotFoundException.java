package com.B2BMarket.ordering.domain.model.shoppingcart;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_NOT_FOUND;

public class ShoppingCartNotFoundException extends DomainException {

    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super(String.format(ERROR_SHOPPING_CART_NOT_FOUND, shoppingCartId));
    }
}
