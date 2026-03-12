package com.B2BMarket.ordering.domain.factory;


import com.B2BMarket.ordering.domain.entity.Order;
import com.B2BMarket.ordering.domain.entity.OrderTestDataBuilder;
import com.B2BMarket.ordering.domain.entity.PaymentMethod;
import com.B2BMarket.ordering.domain.entity.ProductTestDataBuilder;
import com.B2BMarket.ordering.domain.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.valueObject.Product;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.Shipping;
import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

    @Test
    public void shouldGenerateFilledOrderThatCanBePlaced() {
        Shipping shipping = OrderTestDataBuilder.aShipping();
        BillingInfo billing = OrderTestDataBuilder.aBillingInfo();

        Product product = ProductTestDataBuilder.aProduct().build();
        PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();

        Order order = OrderFactory.filled(
                customerId, shipping, billing, paymentMethod, product, quantity
        );

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o -> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
                o -> Assertions.assertThat(o.items()).isNotEmpty(),
                o -> Assertions.assertThat(o.customerId()).isNotNull(),
                o -> Assertions.assertThat(o.isDraft()).isTrue()
        );

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();


    }

}