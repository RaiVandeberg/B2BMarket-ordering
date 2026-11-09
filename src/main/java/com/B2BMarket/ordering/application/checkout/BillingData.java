package com.B2BMarket.ordering.application.checkout;

import com.B2BMarket.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingData {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String document;
    private AddressData address;
}
