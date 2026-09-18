package com.B2BMarket.ordering.infrastructure.persistence.assembler;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order){
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setCustomerId(order.customerId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setCanceledAt(order.cancelledAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        return orderPersistenceEntity;
    }
}
