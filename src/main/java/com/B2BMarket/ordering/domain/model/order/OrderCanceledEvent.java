package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderCanceledEvent(OrderId orderId, CustomerId customerId, OffsetDateTime canceledAt) {
}
