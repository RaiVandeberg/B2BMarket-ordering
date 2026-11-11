package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.DomainService;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;

import java.util.Objects;

@DomainService()
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, Billing billing,
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
