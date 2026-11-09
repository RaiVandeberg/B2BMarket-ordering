package com.B2BMarket.ordering.application.order.management;

import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderNotFoundException;
import com.B2BMarket.ordering.domain.model.order.OrderStatus;
import com.B2BMarket.ordering.domain.model.order.OrderStatusCannotBeChangedException;
import com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder;
import com.B2BMarket.ordering.domain.model.order.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderManagementApplicationServiceIT {

    @Autowired
    private OrderManagementApplicationService orderManagementApplicationService;

    @Autowired
    private Customers customers;

    @Autowired
    private Orders orders;

    @BeforeEach
    public void setUp(){
        if(!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    public void shouldCancel(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        orderManagementApplicationService.cancel(order.id().value().toLong());

        Order canceledOrder = orders.ofId(order.id()).orElseThrow();
        Assertions.assertThat(canceledOrder.status()).isEqualTo(OrderStatus.CANCELLED);
        Assertions.assertThat(canceledOrder.canceledAt()).isNotNull();
    }

    @Test
    public void shouldNotCancelWhenOrderNotFound(){
        Long nonExistentOrderId = 999999999999L;

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> orderManagementApplicationService.cancel(nonExistentOrderId));
    }

    @Test
    public void shouldNotCancelWhenOrderIsAlreadyCanceled(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELLED).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> orderManagementApplicationService.cancel(order.id().value().toLong()));
    }

    @Test
    public void shouldMarkAsPaid(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        orderManagementApplicationService.markAsPaid(order.id().value().toLong());

        Order paidOrder = orders.ofId(order.id()).orElseThrow();
        Assertions.assertThat(paidOrder.status()).isEqualTo(OrderStatus.PAID);
        Assertions.assertThat(paidOrder.paidAt()).isNotNull();
    }

    @Test
    public void shouldNotMarkAsPaidWhenOrderNotFound(){
        Long nonExistentOrderId = 999999999999L;

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> orderManagementApplicationService.markAsPaid(nonExistentOrderId));
    }

    @Test
    public void shouldNotMarkAsPaidWhenOrderIsAlreadyPaid(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> orderManagementApplicationService.markAsPaid(order.id().value().toLong()));
    }

    @Test
    public void shouldMarkAsReady(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order);

        orderManagementApplicationService.markAsReady(order.id().value().toLong());

        Order readyOrder = orders.ofId(order.id()).orElseThrow();
        Assertions.assertThat(readyOrder.status()).isEqualTo(OrderStatus.READY);
        Assertions.assertThat(readyOrder.readyAt()).isNotNull();
    }

    @Test
    public void shouldNotMarkAsReadyWhenOrderNotFound(){
        Long nonExistentOrderId = 999999999999L;

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> orderManagementApplicationService.markAsReady(nonExistentOrderId));
    }

    @Test
    public void shouldNotMarkAsReadyWhenOrderIsNotPaid(){
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> orderManagementApplicationService.markAsReady(order.id().value().toLong()));
    }

}
