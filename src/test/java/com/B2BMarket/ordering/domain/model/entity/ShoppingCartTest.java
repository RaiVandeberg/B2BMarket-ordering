package com.B2BMarket.ordering.domain.model.entity;

import com.B2BMarket.ordering.domain.model.exception.ProductOutOfStockException;
import com.B2BMarket.ordering.domain.model.exception.ShoppingCartDoesNotContainItemException;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.ProductName;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

class ShoppingCartTest {

    @Test
    public void shouldStartAnEmptyShoppingCart() {
        CustomerId customerId = new CustomerId();

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Assertions.assertWith(shoppingCart,
                c -> Assertions.assertThat(c.id()).isNotNull(),
                c -> Assertions.assertThat(c.customerId()).isEqualTo(customerId),
                c -> Assertions.assertThat(c.createdAt()).isNotNull(),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO),
                c -> Assertions.assertThat(c.items()).isEmpty(),
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.containsUnavailableItems()).isFalse()
        );
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));

        Set<ShoppingCartItem> items = shoppingCart.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    public void givenOutOfStockProduct_whenAddItem_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProductUnavailable().build();

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> shoppingCart.addItem(product, new Quantity(1)));

        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    public void givenSameProductTwice_whenAddItem_shouldSumQuantityAndUpdateProductData() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());

        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));
        shoppingCart.addItem(
                ProductTestDataBuilder.aProductAltMousePad()
                        .name(new ProductName("Mouse Pad XL"))
                        .price(new Money("150"))
                        .build(),
                new Quantity(3)
        );

        Assertions.assertThat(shoppingCart.items()).hasSize(1);

        ShoppingCartItem shoppingCartItem = shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD);

        Assertions.assertWith(shoppingCartItem,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(5)),
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("150")),
                i -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad XL")),
                i -> Assertions.assertThat(i.isAvailable()).isTrue(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("750"))
        );

        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("750"));
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(5));
    }

    @Test
    public void givenDifferentProducts_whenAddItem_shouldCreateDistinctItemsAndRecalculateTotals() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());

        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        Assertions.assertThat(shoppingCart.items()).hasSize(2);

        Assertions.assertThat(shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD).totalAmount())
                .isEqualTo(new Money("200"));
        Assertions.assertThat(shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_RAM_MEMORY).totalAmount())
                .isEqualTo(new Money("200"));

        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    public void givenShoppingCartWithItems_whenRemoveItem_shouldRecalculateTotals() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());

        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        ShoppingCartItem mousePadItem = shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD);

        shoppingCart.removeItem(mousePadItem.id());

        Assertions.assertThat(shoppingCart.items()).hasSize(1);
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("200"));
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(1));
    }

    @Test
    public void givenNonExistentItem_whenRemoveItem_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));

        ShoppingCartItemId unknownItemId = new ShoppingCartItemId();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> shoppingCart.removeItem(unknownItemId));

        Assertions.assertThat(shoppingCart.items()).hasSize(1);
    }

    @Test
    public void givenShoppingCartWithItems_whenEmpty_shouldRemoveAllItemsAndZeroTotals() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());

        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        shoppingCart.empty();

        Assertions.assertWith(shoppingCart,
                c -> Assertions.assertThat(c.items()).isEmpty(),
                c -> Assertions.assertThat(c.isEmpty()).isTrue(),
                c -> Assertions.assertThat(c.totalAmount()).isEqualTo(Money.ZERO),
                c -> Assertions.assertThat(c.totalItems()).isEqualTo(Quantity.ZERO)
        );
    }

    @Test
    public void givenShoppingCartWithItems_whenRefreshItem_shouldUpdateItemAndRecalculateTotals() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));

        shoppingCart.refreshItem(ProductTestDataBuilder.aProductAltMousePad()
                .price(new Money("150"))
                .inStock(false)
                .build());

        ShoppingCartItem shoppingCartItem = shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD);

        Assertions.assertThat(shoppingCartItem.price()).isEqualTo(new Money("150"));
        Assertions.assertThat(shoppingCartItem.isAvailable()).isFalse();
        Assertions.assertThat(shoppingCart.containsUnavailableItems()).isTrue();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("300"));
    }

    @Test
    public void givenProductNotInShoppingCart_whenRefreshItem_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));

        Product otherProduct = ProductTestDataBuilder.aProductAltRamMemory().build();

        Assertions.assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> shoppingCart.refreshItem(otherProduct));
    }

    @Test
    public void givenShoppingCartWithItems_whenChangeItemQuantity_shouldRecalculateTotals() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));

        ShoppingCartItem shoppingCartItem = shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD);

        shoppingCart.changeItemQuantity(shoppingCartItem.id(), new Quantity(4));

        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(4));
    }

    @Test
    public void givenZeroQuantity_whenChangeItemQuantity_shouldGenerateException() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));

        ShoppingCartItemId shoppingCartItemId =
                shoppingCart.findItem(ProductTestDataBuilder.PRODUCT_ID_MOUSE_PAD).id();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> shoppingCart.changeItemQuantity(shoppingCartItemId, Quantity.ZERO));

        Assertions.assertThat(shoppingCart.totalItems()).isEqualTo(new Quantity(1));
    }

    @Test
    public void shouldBeEqualWhenIdIsTheSame() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();

        ShoppingCart shoppingCart = ShoppingCart.existing()
                .id(shoppingCartId)
                .customerId(new CustomerId())
                .totalAmount(Money.ZERO)
                .totalItems(Quantity.ZERO)
                .createdAt(OffsetDateTime.now())
                .items(new HashSet<>())
                .build();

        ShoppingCart sameShoppingCart = ShoppingCart.existing()
                .id(shoppingCartId)
                .customerId(new CustomerId())
                .totalAmount(new Money("500"))
                .totalItems(new Quantity(5))
                .createdAt(OffsetDateTime.now().minusDays(1))
                .items(new HashSet<>())
                .build();

        Assertions.assertThat(shoppingCart).isEqualTo(sameShoppingCart);
        Assertions.assertThat(shoppingCart).hasSameHashCodeAs(sameShoppingCart);
    }

}
