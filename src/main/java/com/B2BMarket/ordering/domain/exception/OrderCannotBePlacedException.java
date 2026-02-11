package com.B2BMarket.ordering.domain.exception;

import com.B2BMarket.ordering.domain.valueObject.id.OrderId;

public class OrderCannotBePlacedException extends DomainException {
    public OrderCannotBePlacedException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NOT_ITEMS, id));
    }
}
