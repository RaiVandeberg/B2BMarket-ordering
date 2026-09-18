package com.B2BMarket.ordering.infrastructure.persistence.assembler;

import com.B2BMarket.ordering.domain.model.entity.OrderTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

import static org.assertj.core.api.Assertions.*;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

   @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);
        assertThat(orderPersistenceEntity).satisfies(
                p-> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p-> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.cancelledAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void shouldMerge() {

    }

}