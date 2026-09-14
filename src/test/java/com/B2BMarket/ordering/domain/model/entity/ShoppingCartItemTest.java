package com.B2BMarket.ordering.domain.model.entity;

import com.B2BMarket.ordering.domain.model.exception.ShoppingCartItemIncompatibleProductException;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.ProductName;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.valueObject.id.ShoppingCartItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartItemTest {

    @Test
    public void shouldGenerateBrandNewShoppingCartItem() {
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        Quantity quantity = new Quantity(3);
        ShoppingCartId shoppingCartId = new ShoppingCartId();

        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(shoppingCartId)
                .product(product)
                .quantity(quantity)
                .build();

        Assertions.assertWith(shoppingCartItem,
                i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.shoppingCartId()).isEqualTo(shoppingCartId),
                i -> Assertions.assertThat(i.productId()).isEqualTo(product.id()),
                i -> Assertions.assertThat(i.productName()).isEqualTo(product.name()),
                i -> Assertions.assertThat(i.price()).isEqualTo(product.price()),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(quantity),
                i -> Assertions.assertThat(i.isAvailable()).isTrue(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("300"))
        );
    }

    @Test
    public void givenBrandNewItem_whenChangeQuantity_shouldRecalculateTotalAmount() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(new ShoppingCartId())
                .product(ProductTestDataBuilder.aProductAltMousePad().build())
                .quantity(new Quantity(1))
                .build();

        shoppingCartItem.changeQuantity(new Quantity(5));

        Assertions.assertThat(shoppingCartItem.quantity()).isEqualTo(new Quantity(5));
        Assertions.assertThat(shoppingCartItem.totalAmount()).isEqualTo(new Money("500"));
    }

    @Test
    public void givenBrandNewItem_whenChangeQuantityToZero_shouldGenerateException() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(new ShoppingCartId())
                .product(ProductTestDataBuilder.aProductAltMousePad().build())
                .quantity(new Quantity(1))
                .build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> shoppingCartItem.changeQuantity(Quantity.ZERO));

        Assertions.assertThat(shoppingCartItem.quantity()).isEqualTo(new Quantity(1));
        Assertions.assertThat(shoppingCartItem.totalAmount()).isEqualTo(new Money("100"));
    }

    @Test
    public void givenBrandNewItem_whenRefreshWithSameProduct_shouldUpdateItemData() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(new ShoppingCartId())
                .product(ProductTestDataBuilder.aProductAltMousePad().build())
                .quantity(new Quantity(2))
                .build();

        Product updatedProduct = ProductTestDataBuilder.aProductAltMousePad()
                .name(new ProductName("Mouse Pad XL"))
                .price(new Money("150"))
                .inStock(false)
                .build();

        shoppingCartItem.refresh(updatedProduct);

        Assertions.assertWith(shoppingCartItem,
                i -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad XL")),
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("150")),
                i -> Assertions.assertThat(i.isAvailable()).isFalse(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("300"))
        );
    }

    @Test
    public void givenBrandNewItem_whenRefreshWithIncompatibleProduct_shouldGenerateException() {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(new ShoppingCartId())
                .product(ProductTestDataBuilder.aProductAltMousePad().build())
                .quantity(new Quantity(1))
                .build();

        Product incompatibleProduct = ProductTestDataBuilder.aProductAltRamMemory().build();

        Assertions.assertThatExceptionOfType(ShoppingCartItemIncompatibleProductException.class)
                .isThrownBy(() -> shoppingCartItem.refresh(incompatibleProduct));

        Assertions.assertThat(shoppingCartItem.productName()).isEqualTo(new ProductName("Mouse Pad"));
        Assertions.assertThat(shoppingCartItem.price()).isEqualTo(new Money("100"));
    }

    @Test
    public void shouldBeEqualWhenIdIsTheSame() {
        ShoppingCartItemId shoppingCartItemId = new ShoppingCartItemId();
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        ShoppingCartItem item = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(new ShoppingCartId())
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .quantity(new Quantity(1))
                .totalAmount(new Money("100"))
                .available(true)
                .build();

        ShoppingCartItem sameItem = ShoppingCartItem.existing()
                .id(shoppingCartItemId)
                .shoppingCartId(new ShoppingCartId())
                .productId(ProductTestDataBuilder.aProductAltRamMemory().build().id())
                .productName(new ProductName("4GB RAM"))
                .price(new Money("200"))
                .quantity(new Quantity(4))
                .totalAmount(new Money("800"))
                .available(false)
                .build();

        Assertions.assertThat(item).isEqualTo(sameItem);
        Assertions.assertThat(item).hasSameHashCodeAs(sameItem);
    }

}
