package com.B2BMarket.ordering.domain.model.repository;

import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {
    Optional<ShoppingCart> ofCustomer(CustomerId customerId);
}
