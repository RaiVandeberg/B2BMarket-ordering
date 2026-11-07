package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.DomainService;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.product.Product;

import java.util.Objects;

@DomainService()
public class BuyNowService {

    public Order buyNow(Product product, CustomerId customerId, BillingInfo billing,
                        Shipping shipping, Quantity quantity, PaymentMethod paymentMethod) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(quantity);
        Objects.requireNonNull(paymentMethod);

        product.checkOutOfStock();

        Order order = Order.draft(customerId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        order.addItem(product, quantity);

        order.place();

        return order;
    }
}
