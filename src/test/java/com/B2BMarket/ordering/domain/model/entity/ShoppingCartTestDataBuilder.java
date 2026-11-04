package com.B2BMarket.ordering.domain.model.entity;

import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;

import static com.B2BMarket.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static com.B2BMarket.ordering.domain.model.entity.ProductTestDataBuilder.*;

public class ShoppingCartTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;
    private boolean withItems = true;

    private ShoppingCartTestDataBuilder() {
    }

    public static ShoppingCartTestDataBuilder aShoppingCart() {
        return new ShoppingCartTestDataBuilder();
    }

    public ShoppingCartTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public ShoppingCartTestDataBuilder empty() {
        this.withItems = false;
        return this;
    }

    public ShoppingCartTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public ShoppingCart build() {
        ShoppingCart cart = ShoppingCart.startShopping(customerId);

        if (withItems) {
            cart.addItem(aProduct().build(), new Quantity(2));
            cart.addItem(aProductAltRamMemory().build(), new Quantity(1));
        }

        return cart;
    }
}
