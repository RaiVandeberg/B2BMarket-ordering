package com.B2BMarket.ordering.infrastructure.fake;

import com.B2BMarket.ordering.domain.model.commons.Address;
import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.order.shipping.OriginAddressService;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {
    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .neighborhood("North Carolina")
                .number("100")
                .city("Burenburg")
                .state("Berlin")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
