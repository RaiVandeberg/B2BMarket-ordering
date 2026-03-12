package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.valueObject.Money;
import com.B2BMarket.ordering.domain.valueObject.Product;
import com.B2BMarket.ordering.domain.valueObject.ProductName;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.id.OrderId;
import com.B2BMarket.ordering.domain.valueObject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class OrderItemTest {

    @Test
    public void shouldGenerateBrandNewOrderItem() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(1);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNew()
                .product(product)
                .quantity(quantity)
                .orderId(orderId)
                .build();

        Assertions.assertWith(orderItem,
                o-> Assertions.assertThat(o.id()).isNotNull(),
                o-> Assertions.assertThat(o.productId()).isEqualTo(product.id()),
                o-> Assertions.assertThat(o.productName()).isEqualTo(product.name()),
                o-> Assertions.assertThat(o.price()).isEqualTo(product.price()),
                o-> Assertions.assertThat(o.quantity()).isEqualTo(quantity),
                o-> Assertions.assertThat(o.orderId()).isEqualTo(orderId)
        );
    }
}