package com.B2BMarket.ordering.domain.valueObject;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);

    }

}