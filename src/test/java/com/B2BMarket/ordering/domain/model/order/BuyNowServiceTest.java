package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductOutOfStockException;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aBilling;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aShipping;

class BuyNowServiceTest {

    private final BuyNowService buyNowService = new BuyNowService();

    @Test
    public void givenValidProduct_WhenBuyingNow_ShouldCreatePlacedOrderWithTheItem() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(2);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, DEFAULT_CUSTOMER_ID, billing, shipping, quantity, paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(1);
        OrderItem orderItem = order.items().iterator().next();
        Assertions.assertThat(orderItem.productId()).isEqualTo(product.id());
        Assertions.assertThat(orderItem.quantity()).isEqualTo(quantity);
        Assertions.assertThat(orderItem.price()).isEqualTo(product.price());
        Assertions.assertThat(orderItem.totalAmount()).isEqualTo(new Money("6000.00"));

        Assertions.assertThat(order.totalItems()).isEqualTo(quantity);
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6010.00"));
    }

    @Test
    public void givenProductOutOfStock_WhenBuyingNow_ShouldThrowProductOutOfStockException() {
        Product product = ProductTestDataBuilder.aProductUnavailable().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(1);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        buyNowService.buyNow(product, DEFAULT_CUSTOMER_ID, billing, shipping, quantity, paymentMethod))
                .isInstanceOf(ProductOutOfStockException.class);
    }

    @Test
    public void givenZeroQuantity_WhenBuyingNow_ShouldThrowIllegalArgumentException() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = Quantity.ZERO;
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        buyNowService.buyNow(product, DEFAULT_CUSTOMER_ID, billing, shipping, quantity, paymentMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
