package com.B2BMarket.ordering.domain.model.customer;

import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;

import java.time.OffsetDateTime;
import java.time.OffsetTime;

public record CustomerRegisteredEvent(CustomerId customerId,
                                      OffsetDateTime registeredAt,
                                      FullName fullName,
                                      Email email) {
}
