package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.Specification;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
public class CustomerHasOrderedEnoughAtYearSpecification implements Specification<Customer> {

    private final Orders orders;
    private final long expectedOrderCount;

    @Override
    public boolean isSatisfied(Customer customer) {
        return orders.salesQuantityByCustomerInYear(
                customer.id(),
                Year.now()
        ) >= expectedOrderCount;
    }
}
