package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.entity.PaymentMethod;
import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.entity.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.B2BMarket.ordering.domain.model.utility.DomainService;
import com.B2BMarket.ordering.domain.model.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.Shipping;

import java.util.Objects;

@DomainService()
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, BillingInfo billing,
                          Shipping shipping, PaymentMethod paymentMethod) {
        Objects.requireNonNull(shoppingCart);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(paymentMethod);

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        for (ShoppingCartItem item : shoppingCart.items()) {
            Product product = Product.builder()
                    .id(item.productId())
                    .name(item.productName())
                    .price(item.price())
                    .inStock(item.isAvailable())
                    .build();

            order.addItem(product, item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;
    }
}
