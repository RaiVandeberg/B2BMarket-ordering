package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.product.ProductId;

import java.time.OffsetDateTime;

public record ShoppingCartItemAddedEvent(ShoppingCartId shoppingCartId,
                                         CustomerId customerId,
                                         ProductId productId,
                                         OffsetDateTime addedAt) {
}
