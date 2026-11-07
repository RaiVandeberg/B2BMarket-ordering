package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.ErrorMessages;


public class OrderDoesNotContainOrderItemException extends DomainException {
    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM,  id, orderItemId));
    }
}
