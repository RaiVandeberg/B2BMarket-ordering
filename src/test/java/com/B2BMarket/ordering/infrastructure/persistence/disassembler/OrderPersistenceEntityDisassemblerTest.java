package com.B2BMarket.ordering.infrastructure.persistence.disassembler;


import com.B2BMarket.ordering.domain.model.valueObject.Address;
import com.B2BMarket.ordering.domain.model.valueObject.Document;
import com.B2BMarket.ordering.domain.model.valueObject.Email;
import com.B2BMarket.ordering.domain.model.valueObject.FullName;
import com.B2BMarket.ordering.domain.model.valueObject.Phone;
import com.B2BMarket.ordering.domain.model.valueObject.ProductName;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.ZipCode;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderId;
import com.B2BMarket.ordering.domain.model.valueObject.id.OrderItemId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.entity.OrderItem;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.entity.OrderStatus;
import com.B2BMarket.ordering.domain.model.entity.PaymentMethod;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class OrderPersistenceEntityDisassemblerTest {

    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    public void shouldConvertFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);
        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new OrderId(persistenceEntity.getId())),
                s -> assertThat(s.customerId()).isEqualTo(new CustomerId(persistenceEntity.getCustomerId())),
                s -> assertThat(s.totalAmount()).isEqualTo(new Money (persistenceEntity.getTotalAmount())),
                s -> assertThat(s.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                s -> assertThat(s.placedAt()).isEqualTo(persistenceEntity.getPlacedAt()),
                s -> assertThat(s.paidAt()).isEqualTo(persistenceEntity.getPaidAt()),
                s -> assertThat(s.canceledAt()).isEqualTo(persistenceEntity.getCanceledAt()),
                s -> assertThat(s.readyAt()).isEqualTo(persistenceEntity.getReadyAt()),
                s -> assertThat(s.status()).isEqualTo(OrderStatus.valueOf(persistenceEntity.getStatus())),
                s -> assertThat(s.paymentMethod()).isEqualTo(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
        );
    }

    @Test
    public void shouldConvertBillingAndShippingFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        BillingEmbeddable billing = persistenceEntity.getBilling();
        assertThat(domainEntity.billing()).satisfies(
                b -> assertThat(b.fullName()).isEqualTo(new FullName(billing.getFirstName(), billing.getLastName())),
                b -> assertThat(b.document()).isEqualTo(new Document(billing.getDocument())),
                b -> assertThat(b.phone()).isEqualTo(new Phone(billing.getPhone())),
                b -> assertThat(b.email()).isEqualTo(new Email(billing.getEmail())),
                b -> assertAddress(b.address(), billing.getAddress())
        );

        ShippingEmbeddable shipping = persistenceEntity.getShipping();
        RecipientEmbeddable recipient = shipping.getRecipient();
        assertThat(domainEntity.shipping()).satisfies(
                s -> assertThat(s.cost()).isEqualTo(new Money(shipping.getCost())),
                s -> assertThat(s.expectedDate()).isEqualTo(shipping.getExpectedDate()),
                s -> assertThat(s.recipient().fullName()).isEqualTo(new FullName(recipient.getFirstName(), recipient.getLastName())),
                s -> assertThat(s.recipient().document()).isEqualTo(new Document(recipient.getDocument())),
                s -> assertThat(s.recipient().phone()).isEqualTo(new Phone(recipient.getPhone())),
                s -> assertAddress(s.address(), shipping.getAddress())
        );
    }

    @Test
    public void shouldKeepBillingAndShippingNullWhenPersistenceHasNone() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .billing(null)
                .shipping(null)
                .build();

        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.billing()).isNull();
        assertThat(domainEntity.shipping()).isNull();
    }

    @Test
    public void shouldConvertItemsFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.items()).hasSameSizeAs(persistenceEntity.getItems());

        for (OrderItemPersistenceEntity item : persistenceEntity.getItems()) {
            OrderItem orderItem = domainEntity.items().stream()
                    .filter(i -> i.id().equals(new OrderItemId(item.getId())))
                    .findFirst()
                    .orElseThrow();

            assertThat(orderItem).satisfies(
                    i -> assertThat(i.orderId()).isEqualTo(new OrderId(persistenceEntity.getId())),
                    i -> assertThat(i.productId()).isEqualTo(new ProductId(item.getProductId())),
                    i -> assertThat(i.productName()).isEqualTo(new ProductName(item.getProductName())),
                    i -> assertThat(i.price()).isEqualTo(new Money(item.getPrice())),
                    i -> assertThat(i.quantity()).isEqualTo(new Quantity(item.getQuantity())),
                    i -> assertThat(i.totalAmount()).isEqualTo(new Money(item.getTotalAmount()))
            );
        }
    }

    @Test
    public void shouldKeepItemsEmptyWhenPersistenceHasNone() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .items(new HashSet<>())
                .build();

        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.items()).isEmpty();
    }

    private void assertAddress(Address actual, AddressEmbeddable expected) {
        assertThat(actual).satisfies(
                a -> assertThat(a.street()).isEqualTo(expected.getStreet()),
                a -> assertThat(a.number()).isEqualTo(expected.getNumber()),
                a -> assertThat(a.complement()).isEqualTo(expected.getComplement()),
                a -> assertThat(a.neighborhood()).isEqualTo(expected.getNeighborhood()),
                a -> assertThat(a.city()).isEqualTo(expected.getCity()),
                a -> assertThat(a.state()).isEqualTo(expected.getState()),
                a -> assertThat(a.zipCode()).isEqualTo(new ZipCode(expected.getZipCode()))
        );
    }

}