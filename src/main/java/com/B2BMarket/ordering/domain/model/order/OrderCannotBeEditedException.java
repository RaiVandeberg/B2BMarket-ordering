package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.DomainException;


import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId orderId, OrderStatus orderStatus) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, orderId, orderStatus));
    }
}