package com.B2BMarket.ordering.infrastructure.persistence.order;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.Orders;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceEntityRepository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;

    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = persistenceEntityRepository.findById(
                orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return persistenceEntityRepository.existsById(orderId.value().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.id().value().toLong();

        persistenceEntityRepository.findById(orderId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(aggregateRoot, persistenceEntity ),
                () -> insert(aggregateRoot)
                );
        aggregateRoot.clearDomainEvents();
    }

    @Override
    public long count() {
        return persistenceEntityRepository.count();
    }

    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {

        List<OrderPersistenceEntity> entities = persistenceEntityRepository.placedByCustomerInYear(
                customerId.value(),
                year.getValue()
        );
        return entities.stream().map(disassembler::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
        return this.persistenceEntityRepository.salesQuantityByCustomerInYear(customerId.value(), year.getValue());
    }

    @Override
    public Money totalSoldForCustomer(CustomerId customerId) {
        return new Money(this.persistenceEntityRepository.totalSoldForCustomer(customerId.value()));
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        uptadeVersion(aggregateRoot, persistenceEntity);

    }


    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        uptadeVersion(aggregateRoot, persistenceEntity);

    }

    @SneakyThrows
    private void uptadeVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }





}
