package com.B2BMarket.ordering.domain.entity;


import com.B2BMarket.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.B2BMarket.ordering.domain.valueObject.*;
import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.valueObject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate(){
        Order draft = Order.draft(new CustomerId());
    }

    @Test
    public void shoulAddItem(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Mouse Pad"),
                new Money("100"),
                new Quantity(1)
                );

        Assertions.assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
                );
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Mouse Pad"),
                new Money("100"),
                new Quantity(1)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }


    @Test
    public void shouldCalculateTotals(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Mouse Pad"),
                new Money("100"),
                new Quantity(1)
        );


        order.addItem(
                productId,
                new ProductName("RAM MEMORY"),
                new Money("2000"),
                new Quantity(2)
        );

        Set<OrderItem> items = order.items();

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("4100"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    public void giverDraftOrder_whenPlace_shouldChangeToPlaced(){
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException(){
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange(){
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBillingInfo_shouldAllowChange(){
        Address address = Address.builder()
                .street("Nelson costa")
                .number("1234")
                .neighborhood("Nort Street")
                .complement("House black")
                .city("Berlin")
                .state("Bahia")
                .zipCode(new ZipCode("78487")).build();
        BillingInfo billingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("225-09-1922"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Smith"))
                .build();

        Order order = Order.draft(new CustomerId());
        order.changeBilling(billingInfo);

        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
    }

    @Test
    public void givenDraftOrder_whenChangeShippingInfo_shouldAllowChange(){
        Address address = Address.builder()
                .street("Nelson costa")
                .number("1234")
                .neighborhood("Nort Street")
                .complement("House black")
                .city("Berlin")
                .state("Bahia")
                .zipCode(new ZipCode("78487")).build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .fullName(new FullName("Rai", "Braz"))
                .document(new Document("111-11-1111"))
                .phone(new Phone(" 111-444-2512"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now();

        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shippingInfo),
                o -> Assertions.assertThat(o.shippingCost()).isEqualTo(shippingCost),
                o -> Assertions.assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate)
                );
    }

}