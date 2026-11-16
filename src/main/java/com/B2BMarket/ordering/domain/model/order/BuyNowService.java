package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.DomainService;
import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.product.Product;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@DomainService()
@RequiredArgsConstructor
public class BuyNowService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification;

    public Order buyNow(Product product, Customer customer, Billing billing,
                        Shipping shipping, Quantity quantity, PaymentMethod paymentMethod) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(customer.id());
        Objects.requireNonNull(billing);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(quantity);
        Objects.requireNonNull(paymentMethod);

        product.checkOutOfStock();

        Order order = Order.draft(customer.id());
        order.changeBilling(billing);

        order.changePaymentMethod(paymentMethod);

        order.addItem(product, quantity);

        if(haveFreeShipping(customer)){
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        }else {
            order.changeShipping(shipping);
        }

        order.place();

        return order;
    }

    private boolean haveFreeShipping(Customer customer) {
        return customerHaveFreeShippingSpecification.isSatisfied(customer);
    }
}
