package com.B2BMarket.ordering.infrastructure.beans;

import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.B2BMarket.ordering.domain.model.order.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder.existingCustomer;

@ExtendWith(MockitoExtension.class)
class SpecificationBeanConfigTest {

    @Mock
    private Orders orders;

    private CustomerHaveFreeShippingSpecification specification;

    @BeforeEach
    void setUp() {
        specification = new SpecificationBeanConfig().customerHaveFreeShippingSpecification(orders);
    }

    @Test
    public void givenCustomerWith199PointsAnd2SalesInYear_WhenChecking_ShouldNotHaveFreeShipping() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(199)).build();
        stubSalesInYear(2L);

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }

    @Test
    public void givenCustomerWith200PointsAnd2SalesInYear_WhenChecking_ShouldHaveFreeShipping() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(200)).build();
        stubSalesInYear(2L);

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }

    @Test
    public void givenCustomerWith200PointsAnd1SaleInYear_WhenChecking_ShouldNotHaveFreeShipping() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(200)).build();
        stubSalesInYear(1L);

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }

    @Test
    public void givenCustomerWith1999PointsAndNoSalesInYear_WhenChecking_ShouldNotHaveFreeShipping() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(1999)).build();
        stubSalesInYear(0L);

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }

    @Test
    public void givenCustomerWith2000PointsAndNoSalesInYear_WhenChecking_ShouldHaveFreeShipping() {
        Customer customer = existingCustomer().loyaltyPoints(new LoyaltyPoints(2000)).build();
        stubSalesInYear(0L);

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }

    private void stubSalesInYear(long sales) {
        Mockito.lenient().when(orders.salesQuantityByCustomerInYear(Mockito.any(CustomerId.class), Mockito.any(Year.class)))
                .thenReturn(sales);
    }
}
