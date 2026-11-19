package com.B2BMarket.ordering.application.order.query;

import com.B2BMarket.ordering.application.checkout.RecipientData;
import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.domain.model.commons.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingData {
    private BigDecimal cost;
    private LocalDate expectedDate;
    private RecipientData recipient;
    private AddressData address;
}
