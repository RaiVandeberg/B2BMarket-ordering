package com.B2BMarket.ordering.domain.model.shoppingcart;

import com.B2BMarket.ordering.domain.model.DomainService;
import com.B2BMarket.ordering.domain.model.customer.CustomerAlreadyHaveShoppingCartException;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@DomainService()
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(CustomerId customerId) {
        Objects.requireNonNull(customerId);

        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        if (shoppingCarts.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException(customerId);
        }

        return ShoppingCart.startShopping(customerId);
    }
}
