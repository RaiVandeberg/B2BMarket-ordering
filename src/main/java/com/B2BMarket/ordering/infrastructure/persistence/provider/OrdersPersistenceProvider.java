package com.B2BMarket.ordering.infrastructure.persistence.provider;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.repository.Orders;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository PersistenceEntityRepository;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order aggregateRoot) {
       var persistenceEntity = OrderPersistenceEntity.builder()
                .id(aggregateRoot.id().value().toLong())
                .customerId(aggregateRoot.customerId().value())
                .build();
        PersistenceEntityRepository.saveAndFlush(persistenceEntity);
    }

    @Override
    public int count() {
        return 0;
    }
}
