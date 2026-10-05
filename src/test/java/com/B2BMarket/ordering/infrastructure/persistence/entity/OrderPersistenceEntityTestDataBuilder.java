package com.B2BMarket.ordering.infrastructure.persistence.entity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.B2BMarket.ordering.domain.model.utility.IdGenerator;


public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder(){

    }

    public static OrderPersistenceEntityBuilder existingOrder(){
        return  OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(com.B2BMarket.ordering.domain.model.utility.IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(1000))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now());

    }
}
