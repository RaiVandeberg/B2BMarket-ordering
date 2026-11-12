package com.B2BMarket.ordering.application.checkout;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderPlacedEvent;
import com.B2BMarket.ordering.domain.model.order.Orders;
import com.B2BMarket.ordering.domain.model.order.shipping.ShippingCostService;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.B2BMarket.ordering.infrastructure.listerner.order.OrderEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CheckoutApplicationServiceIT {

    @Autowired
    private CheckoutApplicationService checkoutApplicationService;

    @Autowired
    private Customers customers;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Orders orders;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @BeforeEach
    public void setUp(){
        if(!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }

        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(
                        new Money("10.00"),
                        LocalDate.now().plusDays(3)
                ));
    }

    @Test
    public void shouldCheckout(){
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput(shoppingCart.id().value()).build();

        String orderId = checkoutApplicationService.checkout(input);

        Assertions.assertThat(orderId).isNotBlank();
        Assertions.assertThat(orders.exists(new OrderId(orderId))).isTrue();

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.isEmpty()).isTrue();

        Mockito.verify(orderEventListener, Mockito.times(1))
                .listen(Mockito.any(OrderPlacedEvent.class));
    }

    @Test
    public void shouldNotCheckoutWhenShoppingCartNotFound(){
        UUID nonExistentShoppingCartId = UUID.randomUUID();

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput(nonExistentShoppingCartId).build();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(input));
    }

    @Test
    public void shouldNotCheckoutWhenShoppingCartIsEmpty(){
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().empty().build();
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput(shoppingCart.id().value()).build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(input));
    }

    @Test
    public void shouldNotCheckoutWhenShoppingCartHasUnavailableItems(){
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCart.refreshItem(ProductTestDataBuilder.aProduct().inStock(false).build());
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput(shoppingCart.id().value()).build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutApplicationService.checkout(input));
    }

}
