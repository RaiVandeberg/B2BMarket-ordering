package com.B2BMarket.ordering.domain.model.order;
import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.ErrorMessages;


public class OrderInvalidShippingDeliveryDateException extends DomainException {
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST, id));
    }
}
