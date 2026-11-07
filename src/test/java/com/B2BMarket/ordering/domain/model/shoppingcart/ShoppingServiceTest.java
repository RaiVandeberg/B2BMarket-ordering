package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.B2BMarket.ordering.domain.model.customer.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

    @Mock
    private Customers customers;

    @Mock
    private ShoppingCarts shoppingCarts;

    @InjectMocks
    private ShoppingService shoppingService;

    @Test
    public void givenExistingCustomerWithoutShoppingCart_WhenStartingShopping_ShouldCreateNewShoppingCart() {
        Mockito.when(customers.exists(DEFAULT_CUSTOMER_ID)).thenReturn(true);
        Mockito.when(shoppingCarts.ofCustomer(DEFAULT_CUSTOMER_ID)).thenReturn(Optional.empty());

        ShoppingCart shoppingCart = shoppingService.startShopping(DEFAULT_CUSTOMER_ID);

        Assertions.assertThat(shoppingCart).isNotNull();
        Assertions.assertThat(shoppingCart.customerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();

        Mockito.verify(customers).exists(DEFAULT_CUSTOMER_ID);
        Mockito.verify(shoppingCarts).ofCustomer(DEFAULT_CUSTOMER_ID);
    }

    @Test
    public void givenNonExistingCustomer_WhenStartingShopping_ShouldThrowCustomerNotFoundException() {
        Mockito.when(customers.exists(DEFAULT_CUSTOMER_ID)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> shoppingService.startShopping(DEFAULT_CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class);

        Mockito.verify(customers).exists(DEFAULT_CUSTOMER_ID);
        Mockito.verify(shoppingCarts, Mockito.never()).ofCustomer(Mockito.any());
    }

    @Test
    public void givenCustomerWithExistingShoppingCart_WhenStartingShopping_ShouldThrowCustomerAlreadyHaveShoppingCartException() {
        Mockito.when(customers.exists(DEFAULT_CUSTOMER_ID)).thenReturn(true);

        ShoppingCart existingShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(DEFAULT_CUSTOMER_ID)
                .build();
        Mockito.when(shoppingCarts.ofCustomer(DEFAULT_CUSTOMER_ID)).thenReturn(Optional.of(existingShoppingCart));

        Assertions.assertThatThrownBy(() -> shoppingService.startShopping(DEFAULT_CUSTOMER_ID))
                .isInstanceOf(CustomerAlreadyHaveShoppingCartException.class);

        Mockito.verify(customers).exists(DEFAULT_CUSTOMER_ID);
        Mockito.verify(shoppingCarts).ofCustomer(DEFAULT_CUSTOMER_ID);
    }
}
