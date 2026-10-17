package com.B2BMarket.ordering.infrastructure.persistence.assembler;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.entity.OrderItem;
import com.B2BMarket.ordering.domain.model.valueObject.Address;
import com.B2BMarket.ordering.domain.model.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.model.valueObject.Recipient;
import com.B2BMarket.ordering.domain.model.valueObject.Shipping;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityAssembler {

    public OrderPersistenceEntity fromDomain(Order order){
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value().toLong());
        orderPersistenceEntity.setCustomerId(order.customerId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setCanceledAt(order.canceledAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setVersion(order.version());
        orderPersistenceEntity.setBilling(assembleBilling(order.billing()));
        orderPersistenceEntity.setShipping(assembleShipping(order.shipping()));
        Set<OrderItemPersistenceEntity>mergeItems = mergeItems(order, orderPersistenceEntity);
        orderPersistenceEntity.replaceItems(mergeItems);
        return orderPersistenceEntity;
    }

    private Set<OrderItemPersistenceEntity> mergeItems(Order order, OrderPersistenceEntity orderPersistenceEntity) {
        Set<OrderItem> newOrupdatedItems = order.items();

        if(newOrupdatedItems == null || newOrupdatedItems.isEmpty()){
            return new HashSet<>();
        }

        Set<OrderItemPersistenceEntity> existingItems = orderPersistenceEntity.getItems();
        if(existingItems == null || existingItems.isEmpty()){
            return newOrupdatedItems.stream()
                    .map(orderItem -> fromDomain(orderItem))
                    .collect(Collectors.toSet());
        }

        Map<Long, OrderItemPersistenceEntity> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

        return newOrupdatedItems.stream()
                .map(orderItem -> {
                    OrderItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                            orderItem.id().value().toLong(), new OrderItemPersistenceEntity()
                    );
                    return merge(itemPersistence, orderItem);
                }).collect(Collectors.toSet());
    }

    public OrderItemPersistenceEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemPersistenceEntity(), orderItem);
    }

    private OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderItemPersistenceEntity,
                                             OrderItem orderItem) {
        orderItemPersistenceEntity.setId(orderItem.id().value().toLong());
        orderItemPersistenceEntity.setProductId(orderItem.productId().value());
        orderItemPersistenceEntity.setProductName(orderItem.productName().value());
        orderItemPersistenceEntity.setPrice(orderItem.price().value());
        orderItemPersistenceEntity.setQuantity(orderItem.quantity().value());
        orderItemPersistenceEntity.setTotalAmount(orderItem.totalAmount().value());
        return orderItemPersistenceEntity;
    }

    private BillingEmbeddable assembleBilling(BillingInfo billing) {
        if (billing == null) {
            return null;
        }
        return BillingEmbeddable.builder()
                .firstName(billing.fullName().firstName())
                .lastName(billing.fullName().lastName())
                .document(billing.document().value())
                .phone(billing.phone().value())
                .email(billing.email().value())
                .address(assembleAddress(billing.address()))
                .build();
    }

    private ShippingEmbeddable assembleShipping(Shipping shipping) {
        if (shipping == null) {
            return null;
        }
        return ShippingEmbeddable.builder()
                .cost(shipping.cost().value())
                .expectedDate(shipping.expectedDate())
                .recipient(assembleRecipient(shipping.recipient()))
                .address(assembleAddress(shipping.address()))
                .build();
    }

    private RecipientEmbeddable assembleRecipient(Recipient recipient) {
        return RecipientEmbeddable.builder()
                .firstName(recipient.fullName().firstName())
                .lastName(recipient.fullName().lastName())
                .document(recipient.document().value())
                .phone(recipient.phone().value())
                .build();
    }

    private AddressEmbeddable assembleAddress(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode().value())
                .build();
    }
}
