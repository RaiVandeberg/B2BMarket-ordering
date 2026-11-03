package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.ShoppingCart;
import com.B2BMarket.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.B2BMarket.ordering.domain.model.exception.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.repository.Customers;
import com.B2BMarket.ordering.domain.model.repository.ShoppingCarts;
import com.B2BMarket.ordering.domain.model.utility.DomainService;
import com.B2BMarket.ordering.domain.model.valueObject.id.CustomerId;
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
