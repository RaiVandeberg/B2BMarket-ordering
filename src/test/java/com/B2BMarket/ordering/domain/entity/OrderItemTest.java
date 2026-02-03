package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.valueObject.Money;
import com.B2BMarket.ordering.domain.valueObject.ProductName;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.id.OrderId;
import com.B2BMarket.ordering.domain.valueObject.id.ProductId;
import org.junit.jupiter.api.Test;


class OrderItemTest {

    @Test
    public void shouldGenerate(){
        OrderItem.brandNew()
                .productId( new ProductId())
                .quantity(new Quantity(1))
                .orderId(new OrderId())
                .productName(new ProductName("Mouse Pad"))
                .price(new Money("100"))
                .build();
    }

}