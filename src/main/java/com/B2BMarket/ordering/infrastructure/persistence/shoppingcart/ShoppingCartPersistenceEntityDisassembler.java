package com.B2BMarket.ordering.infrastructure.persistence.shoppingcart;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.product.ProductId;
import com.B2BMarket.ordering.domain.model.product.ProductName;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItemId;
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
