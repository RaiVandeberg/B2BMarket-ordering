package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_ORDER_NOT_FOUND;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(OrderId orderId) {
        super(String.format(ERROR_ORDER_NOT_FOUND, orderId));
    }
}
