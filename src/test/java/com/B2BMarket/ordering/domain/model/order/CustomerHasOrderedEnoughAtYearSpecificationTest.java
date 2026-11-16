package com.B2BMarket.ordering.domain.model.order;

import com.B2BMarket.ordering.domain.model.customer.Customer;
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
class CustomerHasOrderedEnoughAtYearSpecificationTest {

    @Mock
    private Orders orders;

    private CustomerHasOrderedEnoughAtYearSpecification specification;

    @BeforeEach
    void setUp() {
        specification = new CustomerHasOrderedEnoughAtYearSpecification(orders, 2);
    }

    @Test
    public void givenCustomerWithFewerSalesThanExpected_WhenChecking_ShouldNotBeSatisfied() {
        Customer customer = existingCustomer().build();
        Mockito.when(orders.salesQuantityByCustomerInYear(customer.id(), Year.now())).thenReturn(1L);

        Assertions.assertThat(specification.isSatisfied(customer)).isFalse();
    }

    @Test
    public void givenCustomerWithExactlyExpectedSales_WhenChecking_ShouldBeSatisfied() {
        Customer customer = existingCustomer().build();
        Mockito.when(orders.salesQuantityByCustomerInYear(customer.id(), Year.now())).thenReturn(2L);

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }

    @Test
    public void givenCustomerWithMoreSalesThanExpected_WhenChecking_ShouldBeSatisfied() {
        Customer customer = existingCustomer().build();
        Mockito.when(orders.salesQuantityByCustomerInYear(customer.id(), Year.now())).thenReturn(3L);

        Assertions.assertThat(specification.isSatisfied(customer)).isTrue();
    }
}
