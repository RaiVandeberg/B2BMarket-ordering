package com.B2BMarket.ordering.infrastructure.persistence.disassembler;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.entity.OrderStatus;
import com.B2BMarket.ordering.domain.model.entity.PaymentMethod;
import com.B2BMarket.ordering.domain.model.valueObject.Address;
import com.B2BMarket.ordering.domain.model.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.model.valueObject.Document;
import com.B2BMarket.ordering.domain.model.valueObject.Email;
import com.B2BMarket.ordering.domain.model.valueObject.FullName;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Phone;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.Recipient;
import com.B2BMarket.ordering.domain.model.valueObject.Shipping;
import com.B2BMarket.ordering.domain.model.valueObject.ZipCode;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                .build();
    }

    private BillingInfo disassembleBilling(BillingEmbeddable billing) {
        if (billing == null) {
            return null;
        }
        return BillingInfo.builder()
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
