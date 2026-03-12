package com.B2BMarket.ordering.domain.exception;

import com.B2BMarket.ordering.domain.entity.OrderStatus;
import com.B2BMarket.ordering.domain.valueObject.id.OrderId;

import static com.B2BMarket.ordering.domain.exception.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId orderId, OrderStatus orderStatus) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, orderId, orderStatus));
    }
}