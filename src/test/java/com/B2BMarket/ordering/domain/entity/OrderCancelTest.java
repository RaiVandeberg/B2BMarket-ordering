package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderCancelTest {

    @Test
    void givenOrderInCancellableStatus_whenCancel_shouldChangeStatusToCanceled() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.cancel();

        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.cancelledAt()).isNotNull();
    }

    @Test
    void givenAlreadyCanceledOrder_whenCancel_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();
        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);
    }
}