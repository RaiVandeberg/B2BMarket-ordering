package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.DomainService;
import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotBelongsToCustomerException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@DomainService()
public class CheckoutService {

    private final CustomerHaveFreeShippingSpecification haveFreeShippingSpecification;

    public Order checkout(Customer customer,
                          ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(shoppingCart);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(paymentMethod);

        if (!shoppingCart.customerId().equals(customer.id())) {
            throw new ShoppingCartNotBelongsToCustomerException();
        }

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);

        if(haveFreeShipping(customer)){
            Shipping freeShipping = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(freeShipping);
        }else {
            order.changeShipping(shipping);
        }

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

    private boolean haveFreeShipping(Customer customer) {
        return haveFreeShippingSpecification.isSatisfied(customer);
    }
}
