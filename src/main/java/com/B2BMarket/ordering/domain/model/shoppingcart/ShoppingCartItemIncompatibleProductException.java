package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.ErrorMessages;
import com.B2BMarket.ordering.domain.model.product.ProductId;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId shoppingCartItemId,
                                                        ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT,
                shoppingCartItemId, productId));
    }
}
