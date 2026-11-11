package com.B2BMarket.ordering.infrastructure.listerner.customer;

import com.B2BMarket.ordering.domain.model.customer.CustomerArchivedEvent;
import com.B2BMarket.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventListener {

    @EventListener
    public void listen(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent received 1");
    }

    @EventListener
    public void listenSecondary(CustomerRegisteredEvent event){
        log.info("CustomerRegisteredEvent received 2");
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("CustomerArchivedEvent received 1");
    }

}
