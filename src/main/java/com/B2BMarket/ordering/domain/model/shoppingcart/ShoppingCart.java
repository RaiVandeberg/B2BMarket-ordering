package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.AbstractEventSourceEntity;
import com.B2BMarket.ordering.domain.model.AggregateRoot;
import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ShoppingCart
        extends AbstractEventSourceEntity
        implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId id;
    private CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItems;

    private OffsetDateTime createdAt;

    private Set<ShoppingCartItem> items;

    private Long version;

    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
    public ShoppingCart(ShoppingCartId id, CustomerId customerId,
                        Money totalAmount, Quantity totalItems,
                        OffsetDateTime createdAt, Set<ShoppingCartItem> items,
                        Long version) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setCreatedAt(createdAt);
        this.setItems(items);
        this.version = version;
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        ShoppingCart shoppingCart = new ShoppingCart(
                new ShoppingCartId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                OffsetDateTime.now(),
                new HashSet<>(),
                null
        );
        shoppingCart.publishDomainEvent(new ShoppingCartCreatedEvent(
                shoppingCart.id(), shoppingCart.customerId(), shoppingCart.createdAt()));
        return shoppingCart;
    }

    public void empty() {
        this.items.clear();
        this.recalculateTotals();
        this.publishDomainEvent(new ShoppingCartEmptiedEvent(
                this.id(), this.customerId(), OffsetDateTime.now()));
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        product.checkOutOfStock();

        this.searchItem(product.id()).ifPresentOrElse(
                existingItem -> {
                    existingItem.refresh(product);
                    existingItem.changeQuantity(existingItem.quantity().add(quantity));
                },
                () -> this.items.add(ShoppingCartItem.brandNew()
                        .shoppingCartId(this.id())
                        .product(product)
                        .quantity(quantity)
                        .build())
        );

        this.recalculateTotals();
        this.publishDomainEvent(new ShoppingCartItemAddedEvent(
                this.id(), this.customerId(), product.id(), OffsetDateTime.now()));
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);

        ShoppingCartItem shoppingCartItem = this.findItem(shoppingCartItemId);
        this.items.remove(shoppingCartItem);

        this.recalculateTotals();
        this.publishDomainEvent(new ShoppingCartItemRemovedEvent(
                this.id(), this.customerId(), shoppingCartItem.productId(), OffsetDateTime.now()));
    }

    public void refreshItem(Product product) {
        Objects.requireNonNull(product);

        ShoppingCartItem shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);

        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        Objects.requireNonNull(shoppingCartItemId);
        Objects.requireNonNull(quantity);

        ShoppingCartItem shoppingCartItem = this.findItem(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);

        this.recalculateTotals();
    }

    public ShoppingCartItem findItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);
        return this.items().stream()
                .filter(item -> item.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.searchItem(productId)
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), productId));
    }

    public boolean containsUnavailableItems() {
        return this.items().stream().anyMatch(item -> !item.isAvailable());
    }

    public boolean isEmpty() {
        return this.items().isEmpty();
    }

    public ShoppingCartId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(this.items);
    }

    public Long version() {
        return version;
    }

    private Optional<ShoppingCartItem> searchItem(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items().stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst();
    }

    private void recalculateTotals() {
        BigDecimal totalItemsAmount = this.items().stream()
                .map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItemsQuantity = this.items().stream()
                .map(item -> item.quantity().value())
                .reduce(0, Integer::sum);

        this.setTotalAmount(new Money(totalItemsAmount));
        this.setTotalItems(new Quantity(totalItemsQuantity));
    }

    private void setId(ShoppingCartId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        Objects.requireNonNull(createdAt);
        this.createdAt = createdAt;
    }

    private void setItems(Set<ShoppingCartItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
