package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Product;

public interface ShoppingCartProductAdjustmentService {
    void addProduct(Product productId, Money updatedPrice);
    void changeAvailability(Product productId, boolean available);
}
