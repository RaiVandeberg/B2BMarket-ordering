package com.B2BMarket.ordering.domain.model.exception;

import com.B2BMarket.ordering.domain.model.entity.OrderItem;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException {
    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM,  id, orderItemId));
    }
}
