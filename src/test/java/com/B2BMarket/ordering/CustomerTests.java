package com.B2BMarket.ordering;

import com.B2BMarket.ordering.domain.entity.Customer;
import com.B2BMarket.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerTests {

    @Test
    public void testingCustomers(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Rai Braz",
                LocalDate.of(1991, 7, 5),
                "raibs@email.com",
                "487-415-2504",
                "255-08-0578",
                true,
                OffsetDateTime.now()
        );
        customer.addLoyaltyPoints(10);

    }

}
