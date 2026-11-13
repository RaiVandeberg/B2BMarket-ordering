package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartEmptiedEvent(ShoppingCartId shoppingCartId,
                                       CustomerId customerId,
                                       OffsetDateTime emptiedAt) {
}
