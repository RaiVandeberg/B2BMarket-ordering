package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductOutOfStockException;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.existingCustomer;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aBilling;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aShipping;

@ExtendWith(MockitoExtension.class)
class BuyNowServiceTest {

    @Mock
    private Orders orders;

    @InjectMocks
    private BuyNowService buyNowService;

    @Test
    public void givenValidProduct_WhenBuyingNow_ShouldCreatePlacedOrderWithTheItem() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = existingCustomer().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(2);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billing, shipping, quantity, paymentMethod);

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
    public void givenCustomerWith2000LoyaltyPoints_WhenBuyingNow_ShouldCreateOrderWithFreeShipping() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = existingCustomer()
                .loyaltyPoints(new LoyaltyPoints(2000))
                .build();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(2);

        Order order = buyNowService.buyNow(product, customer, aBilling(), shipping, quantity, PaymentMethod.CREDIT_CARD);

        Assertions.assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build());
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6000.00"));
    }

    @Test
    public void givenCustomerWith100LoyaltyPointsAnd2SalesInYear_WhenBuyingNow_ShouldCreateOrderWithFreeShipping() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = existingCustomer()
                .loyaltyPoints(new LoyaltyPoints(100))
                .build();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(2);

        Mockito.when(orders.salesQuantityByCustomerInYear(DEFAULT_CUSTOMER_ID, Year.now())).thenReturn(2L);

        Order order = buyNowService.buyNow(product, customer, aBilling(), shipping, quantity, PaymentMethod.CREDIT_CARD);

        Assertions.assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build());
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6000.00"));

        Mockito.verify(orders).salesQuantityByCustomerInYear(DEFAULT_CUSTOMER_ID, Year.now());
    }

    @Test
    public void givenCustomerWith100LoyaltyPointsAnd1SaleInYear_WhenBuyingNow_ShouldCreateOrderWithShippingCost() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = existingCustomer()
                .loyaltyPoints(new LoyaltyPoints(100))
                .build();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(2);

        Mockito.when(orders.salesQuantityByCustomerInYear(DEFAULT_CUSTOMER_ID, Year.now())).thenReturn(1L);

        Order order = buyNowService.buyNow(product, customer, aBilling(), shipping, quantity, PaymentMethod.CREDIT_CARD);

        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6010.00"));

        Mockito.verify(orders).salesQuantityByCustomerInYear(DEFAULT_CUSTOMER_ID, Year.now());
    }

    @Test
    public void givenProductOutOfStock_WhenBuyingNow_ShouldThrowProductOutOfStockException() {
        Product product = ProductTestDataBuilder.aProductUnavailable().build();
        Customer customer = existingCustomer().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = new Quantity(1);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        buyNowService.buyNow(product, customer, billing, shipping, quantity, paymentMethod))
                .isInstanceOf(ProductOutOfStockException.class);
    }

    @Test
    public void givenZeroQuantity_WhenBuyingNow_ShouldThrowIllegalArgumentException() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = existingCustomer().build();
        Billing billing = aBilling();
        Shipping shipping = aShipping();
        Quantity quantity = Quantity.ZERO;
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        buyNowService.buyNow(product, customer, billing, shipping, quantity, paymentMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
