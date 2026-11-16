package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.Specification;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasEnoughLoyaltyPointsSpecification implements Specification<Customer> {


    private final LoyaltyPoints expectedLoyaltyPoints;

    @Override
    public boolean isSatisfied(Customer customer) {
        return customer.loyaltyPoints().compareTo(expectedLoyaltyPoints) >= 0;
    }
}
