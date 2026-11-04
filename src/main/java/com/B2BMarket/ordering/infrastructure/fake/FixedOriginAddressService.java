package com.B2BMarket.ordering.infrastructure.fake;

import com.B2BMarket.ordering.domain.model.service.OriginAddressService;
import com.B2BMarket.ordering.domain.model.valueObject.Address;
import com.B2BMarket.ordering.domain.model.valueObject.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {
    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .neighborhood("North Carolina")
                .city("Burenburg")
                .state("Berlin")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
