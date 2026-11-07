package com.B2BMarket.ordering.domain.model.customer;
import com.B2BMarket.ordering.domain.model.DomainException;

import static com.B2BMarket.ordering.domain.model.ErrorMessages.ERROR_CANT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY;

public class CantAddLoyaltyPointsOrderIsNotReady extends DomainException {

    public CantAddLoyaltyPointsOrderIsNotReady() {
        super(ERROR_CANT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY);
    }
}
