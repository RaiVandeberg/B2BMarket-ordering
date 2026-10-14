package com.B2BMarket.ordering.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityTest {

    @Test
    void shouldAddItemToOrderBuiltWithImmutableItems() {
        OrderPersistenceEntity order = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        OrderItemPersistenceEntity item = OrderPersistenceEntityTestDataBuilder.existingItem().build();

        order.addItem(item);

        assertThat(order.getItems()).hasSize(3).contains(item);
        assertThat(item.getOrder()).isEqualTo(order);
    }
}
