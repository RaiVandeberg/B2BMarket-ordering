package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.ZipCode;
import lombok.Builder;

import java.time.LocalDate;

public interface ShippingCostService {
    CalculationResult calculate(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination){

    }

    @Builder
    record CalculationResult(Money cost, LocalDate expectedDate){

    }
}
