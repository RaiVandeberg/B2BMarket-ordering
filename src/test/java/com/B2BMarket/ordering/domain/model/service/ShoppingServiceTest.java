package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.B2BMarket.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.B2BMarket.ordering.domain.model.exception.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.repository.Customers;
import com.B2BMarket.ordering.domain.model.repository.ShoppingCarts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.B2BMarket.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

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
