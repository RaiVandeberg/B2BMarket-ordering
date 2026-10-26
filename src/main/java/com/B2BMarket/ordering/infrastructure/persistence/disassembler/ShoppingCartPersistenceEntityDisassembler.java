package com.B2BMarket.ordering.infrastructure.persistence.disassembler;

import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.entity.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.ProductName;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartItemId;
import com.B2BMarket.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity entity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .totalItems(new Quantity(entity.getTotalItems()))
                .createdAt(entity.getCreatedAt())
                .items(disassembleItems(entity.getItems()))
                .version(entity.getVersion())
                .build();
    }

    private Set<ShoppingCartItem> disassembleItems(Set<ShoppingCartItemPersistenceEntity> items) {
        if (items == null || items.isEmpty()) {
            return new HashSet<>();
        }
        return items.stream()
                .map(this::disassembleItem)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private ShoppingCartItem disassembleItem(ShoppingCartItemPersistenceEntity item) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(item.getId()))
                .shoppingCartId(new ShoppingCartId(item.getShoppingCartId()))
                .productId(new ProductId(item.getProductId()))
                .productName(new ProductName(item.getProductName()))
                .price(new Money(item.getPrice()))
                .quantity(new Quantity(item.getQuantity()))
                .totalAmount(new Money(item.getTotalAmount()))
                .available(item.getAvailable())
                .build();
    }
}
