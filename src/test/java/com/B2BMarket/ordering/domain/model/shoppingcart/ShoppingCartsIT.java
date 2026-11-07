package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.B2BMarket.ordering.infrastructure.persistence.provider.ShoppingCartsPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
})
class ShoppingCartsIT {

    private ShoppingCarts shoppingCarts;
    private Customers customers;

    @Autowired
    public ShoppingCartsIT(ShoppingCarts shoppingCarts, Customers customers) {
        this.shoppingCarts = shoppingCarts;
        this.customers = customers;
    }

    @BeforeEach
    public void setUp() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    public void shouldPersistShoppingCartWithItems() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        Optional<ShoppingCart> possibleCart = shoppingCarts.ofId(cart.id());

        assertThat(possibleCart).isPresent();

        ShoppingCart savedCart = possibleCart.get();

        assertThat(savedCart).satisfies(
                s -> assertThat(s.id()).isEqualTo(cart.id()),
                s -> assertThat(s.customerId()).isEqualTo(cart.customerId()),
                s -> assertThat(s.totalAmount()).isEqualTo(cart.totalAmount()),
                s -> assertThat(s.totalItems()).isEqualTo(cart.totalItems()),
                s -> assertThat(s.items()).hasSize(2)
        );
    }

    @Test
    public void shouldCountCarts() {
        assertThat(shoppingCarts.count()).isZero();

        ShoppingCart cart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCart cart2 = ShoppingCartTestDataBuilder.aShoppingCart().build();

        shoppingCarts.add(cart1);
        shoppingCarts.add(cart2);

        assertThat(shoppingCarts.count()).isEqualTo(2L);
    }

    @Test
    public void shouldFindByCustomerId() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Optional<ShoppingCart> possibleCart = shoppingCarts.ofCustomer(customerId);
        assertThat(possibleCart).isPresent();
        assertThat(possibleCart.get().id()).isEqualTo(cart.id());

        assertThat(shoppingCarts.ofCustomer(new CustomerId())).isEmpty();
    }

    @Test
    public void shouldRemoveCart() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        assertThat(shoppingCarts.exists(cart.id())).isTrue();

        shoppingCarts.remove(cart);

        assertThat(shoppingCarts.exists(cart.id())).isFalse();
    }

    @Test
    public void shouldRemoveCartById() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        assertThat(shoppingCarts.exists(cart.id())).isTrue();

        shoppingCarts.remove(cart.id());

        assertThat(shoppingCarts.exists(cart.id())).isFalse();
    }

    @Test
    public void shouldUpdateExistingCart() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        cart = shoppingCarts.ofId(cart.id()).orElseThrow();
        cart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(3));

        shoppingCarts.add(cart);

        ShoppingCart updatedCart = shoppingCarts.ofId(cart.id()).orElseThrow();

        assertThat(updatedCart.items()).hasSize(3);
        assertThat(updatedCart.totalAmount()).isEqualTo(cart.totalAmount());
    }

    @Test
    public void shouldReturnIfCartExists() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        assertThat(shoppingCarts.exists(cart.id())).isTrue();
        assertThat(shoppingCarts.exists(new ShoppingCartId())).isFalse();
    }

    @Test
    public void shouldPersistEmptyCart() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().empty().build();
        shoppingCarts.add(cart);

        ShoppingCart savedCart = shoppingCarts.ofId(cart.id()).orElseThrow();

        assertThat(savedCart.items()).isEmpty();
        assertThat(savedCart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(savedCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    public void shouldRemoveItemsOnUpdate() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(cart);

        cart = shoppingCarts.ofId(cart.id()).orElseThrow();
        cart.empty();

        shoppingCarts.add(cart);

        ShoppingCart updatedCart = shoppingCarts.ofId(cart.id()).orElseThrow();

        assertThat(updatedCart.items()).isEmpty();
        assertThat(updatedCart.totalAmount()).isEqualTo(Money.ZERO);
    }
}
