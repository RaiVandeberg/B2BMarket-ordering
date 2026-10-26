package com.B2BMarket.ordering.infrastructure.persistence.repository;

import com.B2BMarket.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, Long> {
    Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(UUID customerId);
}
