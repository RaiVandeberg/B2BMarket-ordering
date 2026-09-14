package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartItemId;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId shoppingCartItemId,
                                                        ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT,
                shoppingCartItemId, productId));
    }
}
