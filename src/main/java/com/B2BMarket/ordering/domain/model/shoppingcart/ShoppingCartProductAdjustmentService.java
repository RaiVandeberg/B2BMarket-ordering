package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.product.ProductId;

public interface ShoppingCartProductAdjustmentService {
    void adjustPrice(ProductId productId, Money updatedPrice);
    void changeAvailability(ProductId productId, boolean available);
}
