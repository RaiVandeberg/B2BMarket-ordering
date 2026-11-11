package com.B2BMarket.ordering.infrastructure.persistence.order;

import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.order.Billing;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderItem;
import com.B2BMarket.ordering.domain.model.order.OrderItemId;
import com.B2BMarket.ordering.domain.model.order.OrderStatus;
import com.B2BMarket.ordering.domain.model.order.PaymentMethod;
import com.B2BMarket.ordering.domain.model.order.Recipient;
import com.B2BMarket.ordering.domain.model.order.Shipping;
import com.B2BMarket.ordering.domain.model.product.ProductId;
import com.B2BMarket.ordering.domain.model.product.ProductName;
import com.B2BMarket.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .billing(disassembleBilling(persistenceEntity.getBilling()))
                .shipping(disassembleShipping(persistenceEntity.getShipping()))
                .items(disassembleItems(persistenceEntity.getItems()))
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Set<OrderItem> disassembleItems(Set<OrderItemPersistenceEntity> items) {
        if (items == null || items.isEmpty()) {
            return new HashSet<>();
        }
        return items.stream()
                .map(this::disassembleItem)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private OrderItem disassembleItem(OrderItemPersistenceEntity item) {
        return OrderItem.existing()
                .id(new OrderItemId(item.getId()))
                .orderId(new OrderId(item.getOrderId()))
                .productId(new ProductId(item.getProductId()))
                .productName(new ProductName(item.getProductName()))
                .price(new Money(item.getPrice()))
                .quantity(new Quantity(item.getQuantity()))
                .totalAmount(new Money(item.getTotalAmount()))
                .build();
    }

    private Billing disassembleBilling(BillingEmbeddable billing) {
        if (billing == null) {
            return null;
        }
        return Billing.builder()
                .fullName(new FullName(billing.getFirstName(), billing.getLastName()))
                .document(new Document(billing.getDocument()))
                .phone(new Phone(billing.getPhone()))
                .email(new Email(billing.getEmail()))
                .address(disassembleAddress(billing.getAddress()))
                .build();
    }

    private Shipping disassembleShipping(ShippingEmbeddable shipping) {
        if (shipping == null) {
            return null;
        }
        return Shipping.builder()
                .cost(new Money(shipping.getCost()))
                .expectedDate(shipping.getExpectedDate())
                .recipient(disassembleRecipient(shipping.getRecipient()))
                .address(disassembleAddress(shipping.getAddress()))
                .build();
    }

    private Recipient disassembleRecipient(RecipientEmbeddable recipient) {
        return Recipient.builder()
                .fullName(new FullName(recipient.getFirstName(), recipient.getLastName()))
                .document(new Document(recipient.getDocument()))
                .phone(new Phone(recipient.getPhone()))
                .build();
    }

    private Address disassembleAddress(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }
}
