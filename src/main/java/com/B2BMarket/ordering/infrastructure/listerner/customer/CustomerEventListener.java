package com.B2BMarket.ordering.infrastructure.listerner.customer;

import com.B2BMarket.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.B2BMarket.ordering.domain.model.customer.CustomerArchivedEvent;
import com.B2BMarket.ordering.domain.model.customer.CustomerRegisteredEvent;
import  com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import com.B2BMarket.ordering.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationApplicationService;

    private final CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent received 1");
        NotifyNewRegistrationInput input = new NotifyNewRegistrationInput(
               event.customerId().value(),
               event.fullName().firstName(),
               event.email().value()
       );
        customerNotificationApplicationService.notifyNewRegistration(input);
    }


    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent received 1");
    }

    @EventListener
    public void listen(OrderReadyEvent event){
        customerLoyaltyPointsApplicationService.addLoyaltyPoints(event.customerId().value(),
                event.orderId().toString());

    }

}
