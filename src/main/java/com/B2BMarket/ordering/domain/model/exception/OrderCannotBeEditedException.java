package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.entity.OrderStatus;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId orderId, OrderStatus orderStatus) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, orderId, orderStatus));
    }
}