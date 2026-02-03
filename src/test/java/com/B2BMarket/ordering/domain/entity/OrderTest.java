package com.B2BMarket.ordering.domain.entity;


import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    public void shouldGenerate(){
        Order draft = Order.draft(new CustomerId());
    }

}