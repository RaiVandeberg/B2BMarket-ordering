package com.B2BMarket.ordering.application.shoppingcart.management;

import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductCatalogService;
import com.B2BMarket.ordering.domain.model.product.ProductNotFoundException;
import com.B2BMarket.ordering.domain.model.product.ProductOutOfStockException;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartDoesNotContainItemException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCarts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

    @Autowired
    private ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

    @Autowired
    private Customers customers;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @Test
    public void shouldAddItem(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .empty()
                .build();
        shoppingCarts.add(cart);

        Product product = ProductTestDataBuilder.aProduct().build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(cart.id().value())
                .productId(product.id().value())
                .quantity(2)
                .build();

        shoppingCartManagementApplicationService.addItem(input);

        ShoppingCart updatedCart = shoppingCarts.ofId(cart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).hasSize(1);
        Assertions.assertThat(updatedCart.totalItems()).isEqualTo(new Quantity(2));
    }

    @Test
    public void shouldNotAddItemWhenShoppingCartNotFound(){
        Product product = ProductTestDataBuilder.aProduct().build();

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(UUID.randomUUID())
                .productId(product.id().value())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    public void shouldNotAddItemWhenProductNotFound(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .empty()
                .build();
        shoppingCarts.add(cart);

        UUID nonExistentProductId = UUID.randomUUID();
        Mockito.when(productCatalogService.ofId(Mockito.any())).thenReturn(Optional.empty());

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(cart.id().value())
                .productId(nonExistentProductId)
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    public void shouldNotAddItemWhenProductIsOutOfStock(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .empty()
                .build();
        shoppingCarts.add(cart);

        Product product = ProductTestDataBuilder.aProductUnavailable().build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(cart.id().value())
                .productId(product.id().value())
                .quantity(1)
                .build();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.addItem(input));
    }

    @Test
    public void shouldCreateNew(){
        Customer customer = aPersistedCustomer();

        UUID shoppingCartId = shoppingCartManagementApplicationService.createNew(customer.id().value());

        Assertions.assertThat(shoppingCartId).isNotNull();
        Assertions.assertThat(shoppingCarts.exists(new ShoppingCartId(shoppingCartId))).isTrue();
        Assertions.assertThat(shoppingCarts.ofCustomer(customer.id())).isPresent();
    }

    @Test
    public void shouldNotCreateNewWhenCustomerNotFound(){
        UUID nonExistentCustomerId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.createNew(nonExistentCustomerId));
    }

    @Test
    public void shouldNotCreateNewWhenCustomerAlreadyHasShoppingCart(){
        Customer customer = aPersistedCustomer();
        shoppingCarts.add(ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build());

        Assertions.assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.createNew(customer.id().value()));
    }

    @Test
    public void shouldRemoveItem(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build();
        shoppingCarts.add(cart);

        ShoppingCartItem itemToRemove = cart.items().iterator().next();

        shoppingCartManagementApplicationService.removeItem(cart.id().value(), itemToRemove.id().value());

        ShoppingCart updatedCart = shoppingCarts.ofId(cart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).hasSize(1);
        Assertions.assertThat(updatedCart.items())
                .noneMatch(item -> item.id().equals(itemToRemove.id()));
    }

    @Test
    public void shouldNotRemoveItemWhenShoppingCartNotFound(){
        UUID nonExistentCartId = UUID.randomUUID();
        UUID someItemId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.removeItem(nonExistentCartId, someItemId));
    }

    @Test
    public void shouldNotRemoveItemWhenItemNotFound(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build();
        shoppingCarts.add(cart);

        UUID nonExistentItemId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.removeItem(cart.id().value(), nonExistentItemId));
    }

    @Test
    public void shouldEmpty(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build();
        shoppingCarts.add(cart);

        shoppingCartManagementApplicationService.empty(cart.id().value());

        ShoppingCart updatedCart = shoppingCarts.ofId(cart.id()).orElseThrow();
        Assertions.assertThat(updatedCart.items()).isEmpty();
    }

    @Test
    public void shouldNotEmptyWhenShoppingCartNotFound(){
        UUID nonExistentCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.empty(nonExistentCartId));
    }

    @Test
    public void shouldDelete(){
        Customer customer = aPersistedCustomer();
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id())
                .build();
        shoppingCarts.add(cart);

        shoppingCartManagementApplicationService.delete(cart.id().value());

        Assertions.assertThat(shoppingCarts.ofId(cart.id())).isEmpty();
        Assertions.assertThat(shoppingCarts.exists(cart.id())).isFalse();
    }

    @Test
    public void shouldNotDeleteWhenShoppingCartNotFound(){
        UUID nonExistentCartId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> shoppingCartManagementApplicationService.delete(nonExistentCartId));
    }

    private Customer aPersistedCustomer(){
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .build();
        customers.add(customer);
        return customer;
    }

}
