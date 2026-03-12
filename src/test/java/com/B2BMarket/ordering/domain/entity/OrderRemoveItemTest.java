package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.exception.OrderCannotBeEditedException;
import com.B2BMarket.ordering.domain.exception.OrderDoesNotContainOrderItemException;
import com.B2BMarket.ordering.domain.valueObject.Money;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.id.OrderItemId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OrderRemoveItemTest {

    @Test
    void givenDraftOrderWithTwoItems_whenRemoveOneItem_shouldSucceedAndRecalculateTotals() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderItem itemToRemove = order.items().stream().findFirst().orElseThrow();
        BigDecimal expectedTotalAmount = order.totalAmount().value().subtract(itemToRemove.totalAmount().value());
        int expectedTotalItems = 1;

        order.removeItem(itemToRemove.id());

        assertThat(order.items()).hasSize(1);
        assertThat(order.totalAmount()).isEqualTo(new Money(expectedTotalAmount));
        assertThat(order.totalItems()).isEqualTo(new Quantity(expectedTotalItems));
    }

    @Test
    void givenDraftOrder_whenRemoveNonExistentItem_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderItemId nonExistentItemId = new OrderItemId();

        assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(nonExistentItemId));
    }

    @Test
    void givenPlacedOrder_whenRemoveItem_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        OrderItemId itemToRemove = order.items().stream().findFirst().orElseThrow().id();

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(itemToRemove));
    }
}