package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.entity.PaymentMethod;
import com.B2BMarket.ordering.domain.model.utility.DomainService;
import com.B2BMarket.ordering.domain.model.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.Quantity;
import com.B2BMarket.ordering.domain.model.valueObject.Shipping;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;

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
