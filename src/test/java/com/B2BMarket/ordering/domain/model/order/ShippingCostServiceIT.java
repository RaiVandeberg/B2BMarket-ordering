package com.B2BMarket.ordering.domain.model.order;


import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.order.shipping.OriginAddressService;
import com.B2BMarket.ordering.domain.model.order.shipping.ShippingCostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.B2BMarket.ordering.domain.model.order.shipping.ShippingCostService.*;

@SpringBootTest
class ShippingCostServiceIT {

    @Autowired
    private ShippingCostService shippingCostService;

    @Autowired
    private OriginAddressService originAddressService;

    @Test
    void shouldCalculate(){
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode("12345");

        var calculate = shippingCostService
                .calculate(new CalculationRequest(origin, destination));

        Assertions.assertThat(calculate.cost()).isNotNull();
        Assertions.assertThat(calculate.expectedDate()).isNotNull();
    }
}