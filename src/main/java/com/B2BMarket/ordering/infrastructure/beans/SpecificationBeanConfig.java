package com.B2BMarket.ordering.infrastructure.beans;

import com.B2BMarket.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.B2BMarket.ordering.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeanConfig {

    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHaveFreeShippingSpecification(
                orders,
                200,
                2,
                2000
        );
    }

}
