package com.B2BMarket.ordering.infrastructure.persistence.provider;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.repository.Orders;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceEntityRepository;
    private final OrderPersistenceEntityAssembler assembler;

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
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
    }

    @Override
    public int count() {
        return 0;
    }
}
