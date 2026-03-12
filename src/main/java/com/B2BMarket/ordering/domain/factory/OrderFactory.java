package com.B2BMarket.ordering.domain.factory;

import com.B2BMarket.ordering.domain.entity.Order;
import com.B2BMarket.ordering.domain.entity.PaymentMethod;
import com.B2BMarket.ordering.domain.valueObject.BillingInfo;
import com.B2BMarket.ordering.domain.valueObject.Product;
import com.B2BMarket.ordering.domain.valueObject.Quantity;
import com.B2BMarket.ordering.domain.valueObject.Shipping;
import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;

import java.util.Objects;

public class OrderFactory {

    private OrderFactory(){

    }

    public static Order filled(
            CustomerId customerId,
            Shipping shipping,
            BillingInfo billing,
            PaymentMethod paymentMethod,
            Product product,
            Quantity productQuantity
    ){
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(productQuantity);

        Order order = Order.draft(customerId);

        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, productQuantity);

        return order;
    }
}
