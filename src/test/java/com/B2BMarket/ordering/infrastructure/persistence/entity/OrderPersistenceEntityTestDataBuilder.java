package com.B2BMarket.ordering.infrastructure.persistence.entity;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                .placedAt(OffsetDateTime.now())
                .billing(aBilling())
                .shipping(aShipping());

    }

    public static BillingEmbeddable aBilling() {
        return BillingEmbeddable.builder()
                .firstName("John")
                .lastName("Doe")
                .document("225-09-1992")
                .phone("123-111-9911")
                .email("rai.braz@gmail.com")
                .address(anAddress())
                .build();
    }

    public static ShippingEmbeddable aShipping() {
        return ShippingEmbeddable.builder()
                .cost(new BigDecimal("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(RecipientEmbeddable.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .document("112-33-2321")
                        .phone("111-441-1244")
                        .build())
                .build();
    }

    public static AddressEmbeddable anAddress() {
        return AddressEmbeddable.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode("79911")
                .build();
    }
}
