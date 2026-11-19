package com.B2BMarket.ordering.application.customer.query;

import java.util.UUID;

public interface CustomerQueryService {
    CustomerOutput findById(UUID customerId);
}
