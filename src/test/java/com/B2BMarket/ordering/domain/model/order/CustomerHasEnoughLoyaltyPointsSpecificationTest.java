package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.existingCustomer;

class CustomerHasEnoughLoyaltyPointsSpecificationTest {

    private final CustomerHasEnoughLoyaltyPointsSpecification specification =
            new CustomerHasEnoughLoyaltyPointsSpecification(new LoyaltyPoints(100));

    @Test
    public void givenCustomerBelowExpectedPoints_WhenChecking_ShouldNotBeSatisfied() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(99)).build();

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }

    @Test
    public void givenCustomerWithExactlyExpectedPoints_WhenChecking_ShouldBeSatisfied() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(100)).build();

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }

    @Test
    public void givenCustomerAboveExpectedPoints_WhenChecking_ShouldBeSatisfied() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(101)).build();

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }

    @Test
    public void givenCustomerWithZeroPoints_WhenChecking_ShouldNotBeSatisfied() {
        Customer customer = existingCustomer().loyaltyPoints(LoyaltyPoints.ZERO).build();

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }
}
