package com.B2BMarket.ordering.infrastructure.shipping.client.fake;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.order.shipping.ShippingCostService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "B2BMarket.integrations.shipping.provider", havingValue = "FAKE")
public class ShippingCostServiceFakeImp implements ShippingCostService {
    @Override
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(
                new Money("20"),
                LocalDate.now().plusDays(5)
        );
    }
}
