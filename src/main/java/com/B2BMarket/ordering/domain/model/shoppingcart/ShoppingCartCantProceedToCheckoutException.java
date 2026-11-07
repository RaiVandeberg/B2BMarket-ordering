package com.B2BMarket.ordering.domain.model.shoppingcart;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

    public ShoppingCartCantProceedToCheckoutException() {
        super(ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT);
    }
}
