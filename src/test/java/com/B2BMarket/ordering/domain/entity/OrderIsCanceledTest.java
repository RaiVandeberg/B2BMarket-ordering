package com.B2BMarket.ordering.domain.entity;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderIsCanceledTest {

    @Test
    void givenCanceledOrder_whenIsCanceled_shouldReturnTrue() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();

        boolean isCanceled = order.isCanceled();

        assertThat(isCanceled).isTrue();
    }
    @Test
    void givenNonCanceledOrder_whenIsCanceled_shouldReturnFalse() {
        Order order = OrderTestDataBuilder.anOrder().build();

        boolean isCanceled = order.isCanceled();

        assertThat(isCanceled).isFalse();
    }
}