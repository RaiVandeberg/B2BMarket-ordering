package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.exception.OrderCannotBeEditedException;
import com.B2BMarket.ordering.domain.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.valueObject.Product;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.Shipping;
import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderChangingTest {

    @Test
    void givenDraftOrder_whenChangingAllEditableFields_shouldSucceed() {
        // given
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        BillingInfo billing = OrderTestDataBuilder.aBillingInfo();
        Shipping shipping = OrderTestDataBuilder.aShipping();

        // when & then
        Assertions.assertThatCode(() -> {
            order.addItem(product, new Quantity(1));
            order.changeItemQuantity(order.items().iterator().next().id(), new Quantity(2));
            order.changeBilling(billing);
            order.changeShipping(shipping);
            order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        }).doesNotThrowAnyException();

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(2));
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }


    @Test
    void givenPlacedOrder_whenAddItem_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(product, new Quantity(1)));
    }

    @Test
    void givenPlacedOrder_whenChangeItemQuantity_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        OrderItem item = order.items().iterator().next();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeItemQuantity(item.id(), new Quantity(5)));
    }

    @Test
    void givenPlacedOrder_whenChangeBilling_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        BillingInfo newBilling = OrderTestDataBuilder.aBillingInfo();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBilling(newBilling));
    }

    @Test
    void givenPlacedOrder_whenChangeShipping_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Shipping newShipping = OrderTestDataBuilder.aShipping();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(newShipping));
    }

    @Test
    void givenPlacedOrder_whenChangePaymentMethod_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE));
    }
}