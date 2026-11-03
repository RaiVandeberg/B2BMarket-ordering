package com.B2BMarket.ordering.domain.model.exception;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {

    public ShoppingCartCantProceedToCheckoutException() {
        super(ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT);
    }
}
