package com.B2BMarket.ordering.infrastructure.listerner.shoppingcart;

import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartEmptiedEvent;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItemAddedEvent;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItemRemovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event) {
        log.info("ShoppingCartCreatedEvent received");
    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event) {
        log.info("ShoppingCartEmptiedEvent received");
    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event) {
        log.info("ShoppingCartItemAddedEvent received");
    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event) {
        log.info("ShoppingCartItemRemovedEvent received");
    }

}
