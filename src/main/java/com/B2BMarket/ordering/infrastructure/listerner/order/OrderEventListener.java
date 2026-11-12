package com.B2BMarket.ordering.infrastructure.listerner.order;

import com.B2BMarket.ordering.domain.model.order.OrderCanceledEvent;
import com.B2BMarket.ordering.domain.model.order.OrderPaidEvent;
import com.B2BMarket.ordering.domain.model.order.OrderPlacedEvent;
import com.B2BMarket.ordering.domain.model.order.OrderReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @EventListener
    public void listen(OrderPlacedEvent event) {
        log.info("OrderPlacedEvent received");
    }

    @EventListener
    public void listen(OrderPaidEvent event) {
        log.info("OrderPaidEvent received");
    }

    @EventListener
    public void listen(OrderReadyEvent event) {
        log.info("OrderReadyEvent received");
    }

    @EventListener
    public void listen(OrderCanceledEvent event) {
        log.info("OrderCanceledEvent received");
    }

}
