package com.B2BMarket.ordering.infrastructure.fake;

import com.B2BMarket.ordering.domain.model.service.ShippingCostService;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ShippingCostServiceFakeImp implements ShippingCostService {
    @Override
    public CalculationResult calculate(CalculationRequest request) {
        return new CalculationResult(
                new Money("20"),
                LocalDate.now().plusDays(5)
        );
    }
}
