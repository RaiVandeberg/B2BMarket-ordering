package com.B2BMarket.ordering.infrastructure.persistence.shoppingcart;

import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity,
                                               ShoppingCart shoppingCart) {
        persistenceEntity.setId(shoppingCart.id().value());
        persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        persistenceEntity.setTotalItems(shoppingCart.totalItems().value());
        persistenceEntity.setCreatedAt(shoppingCart.createdAt());
        persistenceEntity.setVersion(shoppingCart.version());

        CustomerPersistenceEntity customer = customerPersistenceEntityRepository
                .getReferenceById(shoppingCart.customerId().value());
        persistenceEntity.setCustomer(customer);

        Set<ShoppingCartItemPersistenceEntity> mergedItems = mergeItems(shoppingCart, persistenceEntity);
        persistenceEntity.replaceItems(mergedItems);

        return persistenceEntity;
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCart shoppingCart,
                                                              ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        Set<ShoppingCartItem> newOrUpdateItems = shoppingCart.items();
        if (newOrUpdateItems == null || shoppingCart.items().isEmpty()) {
            return new HashSet<>();
        }

        Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistenceEntity.getItems();

        if (existingItems == null || existingItems.isEmpty()) {
            return newOrUpdateItems.stream().map(this::fromDomain).collect(Collectors.toSet());
        }

        Map<UUID, ShoppingCartItemPersistenceEntity> existingMap = existingItems.stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, item -> item));

        return newOrUpdateItems.stream()
                .map(shoppingCartItem -> {
                    ShoppingCartItemPersistenceEntity itemPersistence = existingMap.getOrDefault(
                            shoppingCartItem.id().value(), new ShoppingCartItemPersistenceEntity()
                    );
                    return mergeItem(itemPersistence, shoppingCartItem);
                }).collect(Collectors.toSet());
    }

    private ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem item) {
        return mergeItem(new ShoppingCartItemPersistenceEntity(), item);
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity entity,
                                                        ShoppingCartItem item) {
        entity.setId(item.id().value());
        entity.setProductId(item.productId().value());
        entity.setProductName(item.productName().value());
        entity.setPrice(item.price().value());
        entity.setQuantity(item.quantity().value());
        entity.setTotalAmount(item.totalAmount().value());
        entity.setAvailable(item.isAvailable());
        return entity;
    }
}
