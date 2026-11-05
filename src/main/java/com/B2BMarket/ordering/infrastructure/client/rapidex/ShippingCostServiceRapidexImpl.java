package com.B2BMarket.ordering.infrastructure.client.rapidex;

import com.B2BMarket.ordering.domain.model.service.ShippingCostService;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "B2BMarket.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapidexImpl implements ShippingCostService {

    private final RapiDexAPIClient rapiDexAPIClient;

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        DeliveryCostResponse response = rapiDexAPIClient.calculate(
                new DeliveryCostRequest(
                        request.origin().value(),
                        request.destination().value()
                )
        );

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(response.getEstimatedDaysToDeliver());
        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expectedDate(expectedDeliveryDate)
                .build();

    }
}
