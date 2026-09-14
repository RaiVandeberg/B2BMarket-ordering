package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartItemId;

public class ShoppingCartDoesNotContainItemException extends DomainException {

    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, shoppingCartItemId));
    }

    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
