package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aBilling;
import static com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder.aShipping;
import static com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder.aShoppingCart;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {


    private CheckoutService checkoutService;

    @Mock
    private Orders orders;

    @BeforeEach
    void setUp() {
        var specification = new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
        checkoutService = new CheckoutService(specification);
    }

    @Test
    public void givenValidShoppingCart_WhenCheckingOut_ShouldCreatePlacedOrderAndEmptyCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        ShoppingCart shoppingCart = aShoppingCart().customerId(customer.id()).build();
        List<ShoppingCartItem> cartItems = List.copyOf(shoppingCart.items());

        Billing billing = aBilling();
        Shipping shipping = aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = checkoutService.checkout(customer, shoppingCart, billing, shipping, paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(cartItems.size());

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = order.items().stream()
                    .filter(item -> item.productId().equals(cartItem.productId()))
                    .findFirst()
                    .orElseThrow();

            Assertions.assertThat(orderItem.quantity()).isEqualTo(cartItem.quantity());
            Assertions.assertThat(orderItem.price()).isEqualTo(cartItem.price());
            Assertions.assertThat(orderItem.totalAmount()).isEqualTo(cartItem.totalAmount());
        });

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6210.00"));

        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    public void givenCustomerWith3000LoyaltyPoints_WhenCheckingOut_ShouldCreateOrderWithFreeShipping() {
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .loyaltyPoints(new LoyaltyPoints(3000))
                .build();

        ShoppingCart shoppingCart = aShoppingCart().customerId(customer.id()).build();

        Shipping shipping = aShipping();

        Order order = checkoutService.checkout(customer, shoppingCart, aBilling(), shipping, PaymentMethod.CREDIT_CARD);

        Assertions.assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build());
        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("6200.00"));
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    public void givenShoppingCartWithUnavailableItems_WhenCheckingOut_ShouldThrowExceptionAndKeepCartUnchanged() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        ShoppingCart shoppingCart = aShoppingCart().customerId(customer.id()).build();
        shoppingCart.refreshItem(ProductTestDataBuilder.aProduct().inStock(false).build());

        Billing billing = aBilling();
        Shipping shipping = aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        checkoutService.checkout(customer, shoppingCart, billing, shipping, paymentMethod))
                .isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);

        Assertions.assertThat(shoppingCart.isEmpty()).isFalse();
        Assertions.assertThat(shoppingCart.items()).hasSize(2);
        Assertions.assertThat(shoppingCart.containsUnavailableItems()).isTrue();
    }

    @Test
    public void givenEmptyShoppingCart_WhenCheckingOut_ShouldThrowExceptionAndKeepCartEmpty() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        ShoppingCart shoppingCart = aShoppingCart().customerId(customer.id()).empty().build();

        Billing billing = aBilling();
        Shipping shipping = aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatThrownBy(() ->
                        checkoutService.checkout(customer, shoppingCart, billing, shipping, paymentMethod))
                .isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);

        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
    }
}
