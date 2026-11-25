package com.B2BMarket.ordering.application.shoppingcart.query;

import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCarts;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class ShoppingCartQueryServiceIT {

    @Autowired
    private ShoppingCartQueryService queryService;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Test
    public void shouldFindById() {
        ShoppingCart cart = aPersistedShoppingCart();

        ShoppingCartOutput output = queryService.findById(cart.id().value());

        assertOutputMatchesCart(output, cart);
    }

    @Test
    public void givenNonExistentId_whenFindById_shouldThrowShoppingCartNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> queryService.findById(nonExistentId));
    }

    @Test
    public void shouldFindByCustomerId() {
        ShoppingCart cart = aPersistedShoppingCart();

        ShoppingCartOutput output = queryService.findByCustomerId(cart.customerId().value());

        assertOutputMatchesCart(output, cart);
    }

    @Test
    public void givenNonExistentCustomerId_whenFindByCustomerId_shouldThrowShoppingCartNotFoundException() {
        UUID nonExistentCustomerId = new CustomerId().value();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> queryService.findByCustomerId(nonExistentCustomerId));
    }

    private void assertOutputMatchesCart(ShoppingCartOutput output, ShoppingCart cart) {
        Assertions.assertThat(output).isNotNull();
        Assertions.assertThat(output)
                .extracting(
                        ShoppingCartOutput::getId,
                        ShoppingCartOutput::getCustomerId,
                        ShoppingCartOutput::getTotalItems,
                        ShoppingCartOutput::getTotalAmount
                ).containsExactly(
                        cart.id().value(),
                        cart.customerId().value(),
                        cart.totalItems().value(),
                        cart.totalAmount().value()
                );

        Assertions.assertThat(output.getItems()).hasSize(cart.items().size());
        Assertions.assertThat(output.getItems())
                .extracting(
                        ShoppingCartItemOutput::getId,
                        ShoppingCartItemOutput::getProductId,
                        ShoppingCartItemOutput::getName,
                        ShoppingCartItemOutput::getPrice,
                        ShoppingCartItemOutput::getQuantity,
                        ShoppingCartItemOutput::getTotalAmount,
                        ShoppingCartItemOutput::getAvailable
                ).containsExactlyInAnyOrderElementsOf(toTuples(cart));
    }

    private List<Tuple> toTuples(ShoppingCart cart) {
        return cart.items().stream()
                .map(this::toTuple)
                .toList();
    }

    private Tuple toTuple(ShoppingCartItem item) {
        return Assertions.tuple(
                item.id().value(),
                item.productId().value(),
                item.productName().value(),
                item.price().value(),
                item.quantity().value(),
                item.totalAmount().value(),
                item.isAvailable()
        );
    }

    private ShoppingCart aPersistedShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .build();
        customers.add(customer);

        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build();
        shoppingCarts.add(cart);
        return cart;
    }
}
