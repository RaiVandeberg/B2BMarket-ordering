package com.B2BMarket.ordering.infrastructure.persistence.provider;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.repository.Customers;
import com.B2BMarket.ordering.domain.model.valueObject.Email;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
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
public class CustomersPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository persistenceEntityRepository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        Optional<CustomerPersistenceEntity> possibleEntity = persistenceEntityRepository.findById(
                customerId.value());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return persistenceEntityRepository.existsById(customerId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Customer aggregateRoot) {
        persistenceEntityRepository.findById(aggregateRoot.id().value())
                .ifPresentOrElse(
                        (persistenceEntity) -> update(aggregateRoot, persistenceEntity),
                        () -> insert(aggregateRoot)
                );
    }

    @Override
    public long count() {
        return persistenceEntityRepository.count();
    }

    private void update(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void insert(Customer aggregateRoot) {
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }

    @Override
    public Optional<Customer> ofEmail(Email email) {
        return  persistenceEntityRepository.findByEmail(email.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean isEmailUnique(Email email, CustomerId exceptCustomerId) {
        return !persistenceEntityRepository.existsByEmailAndIdNot(email.value(), exceptCustomerId.value());
    }
}
