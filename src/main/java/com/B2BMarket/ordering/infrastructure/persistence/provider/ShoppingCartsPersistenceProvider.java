package com.B2BMarket.ordering.infrastructure.persistence.provider;

import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.repository.ShoppingCarts;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;
    private final ShoppingCartPersistenceEntityAssembler assembler;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return persistenceEntityRepository.findById(shoppingCartId.value().toLong())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return persistenceEntityRepository.existsById(shoppingCartId.value().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(ShoppingCart aggregateRoot) {
        long cartId = aggregateRoot.id().value().toLong();

        persistenceEntityRepository.findById(cartId)
                .ifPresentOrElse(
                        persistenceEntity -> update(aggregateRoot, persistenceEntity),
                        () -> insert(aggregateRoot)
                );
    }

    @Override
    public long count() {
        return persistenceEntityRepository.count();
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return persistenceEntityRepository.findByCustomer_Id(customerId.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCart shoppingCart) {
        persistenceEntityRepository.deleteById(shoppingCart.id().value().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCartId shoppingCartId) {
        persistenceEntityRepository.deleteById(shoppingCartId.value().toLong());
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        uptadeVersion(aggregateRoot, persistenceEntity);
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        uptadeVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void uptadeVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }
}
