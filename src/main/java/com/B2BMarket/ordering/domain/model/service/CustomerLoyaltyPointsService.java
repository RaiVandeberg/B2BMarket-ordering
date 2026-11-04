package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.entity.Order;
import com.B2BMarket.ordering.domain.model.exception.CantAddLoyaltyPointsOrderIsNotReady;
import com.B2BMarket.ordering.domain.model.exception.OrderNotBelongsToCustomerException;
import com.B2BMarket.ordering.domain.model.utility.DomainService;
import com.B2BMarket.ordering.domain.model.valueObject.LoyaltyPoints;
import com.B2BMarket.ordering.domain.model.valueObject.Money;

import java.util.Objects;

@DomainService()
public class CustomerLoyaltyPointsService {

    private static final LoyaltyPoints basePoints = new LoyaltyPoints(5);
    private static final Money expectedAmountToGivePoints = new Money("1000");

    public void addPoints(Customer customer, Order order) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(order);

        if(!customer.id().equals(order.customerId())){
            throw  new OrderNotBelongsToCustomerException();
        }

        if (!order.isReady()) {
            throw  new CantAddLoyaltyPointsOrderIsNotReady();
        }

        customer.addLoyaltyPoints(calculatePoints(order));


    }

    private LoyaltyPoints calculatePoints(Order order) {
        if(shouldGivePointsByAmount(order.totalAmount())){
            Money result = order.totalAmount().divide(expectedAmountToGivePoints);
            return new LoyaltyPoints(result.value().intValue() * basePoints.value()) ;
        }
        return LoyaltyPoints.ZERO;
    }

    private boolean shouldGivePointsByAmount(Money amount) {
        return amount.compareTo(expectedAmountToGivePoints) >= 0;
    }
}
