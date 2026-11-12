package com.B2BMarket.ordering.infrastructure.listerner.customer;

import com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.B2BMarket.ordering.domain.model.customer.CustomerArchivedEvent;
import com.B2BMarket.ordering.domain.model.customer.CustomerRegisteredEvent;
import  com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService.NotifyNewRegistrationInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent received 1");
       CustomerNotificationApplicationService.NotifyNewRegistrationInput input =new NotifyNewRegistrationInput(
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

}
