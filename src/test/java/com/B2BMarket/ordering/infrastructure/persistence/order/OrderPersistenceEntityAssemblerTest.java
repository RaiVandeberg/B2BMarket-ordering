package com.B2BMarket.ordering.infrastructure.persistence.order;

import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.order.Billing;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderItem;
import com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder;
import com.B2BMarket.ordering.domain.model.order.PaymentMethod;
import com.B2BMarket.ordering.domain.model.order.Shipping;
import com.B2BMarket.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderPersistenceEntityAssemblerTest {

    @Mock
    private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @InjectMocks
    private OrderPersistenceEntityAssembler assembler;

    @BeforeEach
    public void setUp() {
        Mockito.when(customerPersistenceEntityRepository.getReferenceById(Mockito.any(UUID.class)))
                .then(a -> {
                    UUID customerId = a.getArgument(0, UUID.class);
                    return CustomerPersistenceEntityTestDataBuilder.existingCustomer().id(customerId).build();
                });
    }

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
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.canceledAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void shouldConvertBillingAndShipping() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        Billing billing = order.billing();
        assertThat(orderPersistenceEntity.getBilling()).satisfies(
                b -> assertThat(b.getFirstName()).isEqualTo(billing.fullName().firstName()),
                b -> assertThat(b.getLastName()).isEqualTo(billing.fullName().lastName()),
                b -> assertThat(b.getDocument()).isEqualTo(billing.document().value()),
                b -> assertThat(b.getPhone()).isEqualTo(billing.phone().value()),
                b -> assertThat(b.getEmail()).isEqualTo(billing.email().value()),
                b -> assertAddress(b.getAddress(), billing.address())
        );

        Shipping shipping = order.shipping();
        assertThat(orderPersistenceEntity.getShipping()).satisfies(
                s -> assertThat(s.getCost()).isEqualTo(shipping.cost().value()),
                s -> assertThat(s.getExpectedDate()).isEqualTo(shipping.expectedDate()),
                s -> assertThat(s.getRecipient().getFirstName()).isEqualTo(shipping.recipient().fullName().firstName()),
                s -> assertThat(s.getRecipient().getLastName()).isEqualTo(shipping.recipient().fullName().lastName()),
                s -> assertThat(s.getRecipient().getDocument()).isEqualTo(shipping.recipient().document().value()),
                s -> assertThat(s.getRecipient().getPhone()).isEqualTo(shipping.recipient().phone().value()),
                s -> assertAddress(s.getAddress(), shipping.address())
        );
    }

    @Test
    void shouldKeepBillingAndShippingNullWhenOrderHasNone() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);

        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        assertThat(orderPersistenceEntity.getBilling()).isNull();
        assertThat(orderPersistenceEntity.getShipping()).isNull();
    }

    private void assertAddress(AddressEmbeddable actual, Address expected) {
        assertThat(actual).satisfies(
                a -> assertThat(a.getStreet()).isEqualTo(expected.street()),
                a -> assertThat(a.getNumber()).isEqualTo(expected.number()),
                a -> assertThat(a.getComplement()).isEqualTo(expected.complement()),
                a -> assertThat(a.getNeighborhood()).isEqualTo(expected.neighborhood()),
                a -> assertThat(a.getCity()).isEqualTo(expected.city()),
                a -> assertThat(a.getState()).isEqualTo(expected.state()),
                a -> assertThat(a.getZipCode()).isEqualTo(expected.zipCode().value())
        );
    }

    @Test
    void givenOrderWithNotItems_shouldRemovePersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntityItems() {
       Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
       OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(new HashSet<>()).build();

       Assertions.assertThat(order.items()).isNotEmpty();
       Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();

       assembler.merge(orderPersistenceEntity, order);

       Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
       Assertions.assertThat(orderPersistenceEntity.getItems()).size().isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldRemoveMergeCorrectly() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();

        Assertions.assertThat(order.items().size()).isEqualTo(2);

        Set<OrderItemPersistenceEntity> orderItemPersistenceEntities = order.items().stream()
                .map(i -> assembler.fromDomain(i))
                .collect(Collectors.toSet());

        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .items(orderItemPersistenceEntities)
                .build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(persistenceEntity, order);

        Assertions.assertThat(persistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(persistenceEntity.getItems()).size().isEqualTo(order.items().size());
    }

}