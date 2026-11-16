package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.Specification;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

import java.time.Year;


@RequiredArgsConstructor
public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

    private final Orders orders;

    private final int minPointsForFreeShippingRule1;
    private final long salesQuantityForFreeShippingRule;

    private final int minPointsForFreeShippingRule2;


    @Override
    public boolean isSatisfied(Customer customer) {
        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule1)) >= 0
                && orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= salesQuantityForFreeShippingRule
                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule2)) >= 0;
    }
}
