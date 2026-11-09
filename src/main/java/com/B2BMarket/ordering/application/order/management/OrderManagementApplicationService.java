package com.B2BMarket.ordering.application.order.management;

import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderNotFoundException;
import com.B2BMarket.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    private final Orders orders;

    @Transactional
    public void cancel(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId id = new OrderId(rawOrderId);
        Order order = orders.ofId(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.cancel();

        orders.add(order);
    }

    @Transactional
    public void markAsPaid(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId id = new OrderId(rawOrderId);
        Order order = orders.ofId(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.markAsPaid();

        orders.add(order);
    }

    @Transactional
    public void markAsReady(Long rawOrderId){
        Objects.requireNonNull(rawOrderId);
        OrderId id = new OrderId(rawOrderId);
        Order order = orders.ofId(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.markAsReady();

        orders.add(order);
    }

}
