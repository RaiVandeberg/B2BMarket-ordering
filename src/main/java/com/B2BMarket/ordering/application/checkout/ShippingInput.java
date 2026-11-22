package com.B2BMarket.ordering.application.checkout;

import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.application.order.query.RecipientData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInput {
    private RecipientData recipient;
    private AddressData address;
}
