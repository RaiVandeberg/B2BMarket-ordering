package com.B2BMarket.ordering.infrastructure.persistence.entity;

import com.B2BMarket.ordering.domain.model.utility.IdGenerator;
import com.B2BMarket.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class ShoppingCartPersistenceEntityTestDataBuilder {

    private ShoppingCartPersistenceEntityTestDataBuilder() {
    }

    public static ShoppingCartPersistenceEntityBuilder existingShoppingCart() {
        return ShoppingCartPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.existingCustomer().build())
                .totalAmount(new BigDecimal("6200"))
                .totalItems(3)
                .createdAt(OffsetDateTime.now())
                .items(Set.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static ShoppingCartItemPersistenceEntity.ShoppingCartItemPersistenceEntityBuilder existingItem() {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTimeBasedUUID())
                .productName("Notebook X11")
                .price(new BigDecimal("3000"))
                .quantity(2)
                .totalAmount(new BigDecimal("6000"))
                .available(true);
    }

    public static ShoppingCartItemPersistenceEntity.ShoppingCartItemPersistenceEntityBuilder existingItemAlt() {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTimeBasedUUID())
                .productName("4GB RAM")
                .price(new BigDecimal("200"))
                .quantity(1)
                .totalAmount(new BigDecimal("200"))
                .available(true);
    }
}
