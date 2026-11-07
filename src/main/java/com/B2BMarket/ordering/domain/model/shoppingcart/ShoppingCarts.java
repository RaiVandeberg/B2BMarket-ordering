package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.RemoveCapableRepository;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {
    Optional<ShoppingCart> ofCustomer(CustomerId customerId);
}
