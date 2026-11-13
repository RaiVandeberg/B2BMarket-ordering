package com.B2BMarket.ordering.application.customer.loyalty;

import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerArchivedException;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerNotFoundException;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderNotBelongsToCustomerException;
import com.B2BMarket.ordering.domain.model.order.OrderNotFoundException;
import com.B2BMarket.ordering.domain.model.order.OrderStatus;
import com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder;
import com.B2BMarket.ordering.domain.model.order.Orders;
import com.B2BMarket.ordering.domain.model.customer.CantAddLoyaltyPointsOrderIsNotReady;
import com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder;
import com.B2BMarket.ordering.infrastructure.listerner.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerLoyaltyPointsApplicationServiceIT {

    @Autowired
    private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @Autowired
    private Customers customers;

    @Autowired
    private Orders orders;

    // Isola o application service: sem isso, o OrderReadyEvent dos pedidos de fixture
    // dispara o CustomerEventListener, que já adiciona os pontos no orders.add().
    @MockitoBean
    private CustomerEventListener customerEventListener;

    @Test
    public void shouldAddLoyaltyPoints(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(order);

        customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().toString());

        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();
        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenCustomerNotFound(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(order);

        UUID nonExistentCustomerId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(nonExistentCustomerId, order.id().toString()));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenOrderNotFound(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        String nonExistentOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), nonExistentOrderId));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenCustomerIsArchived(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customer.archive();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().toString()));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenOrderDoesNotBelongToCustomer(){
        Customer customerA = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("customerA@email.com"))
                .build();
        customers.add(customerA);

        Customer customerB = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .email(new Email("customerB@email.com"))
                .build();
        customers.add(customerB);

        Order orderOfCustomerB = OrderTestDataBuilder.anOrder()
                .customerId(customerB.id())
                .status(OrderStatus.READY)
                .build();
        orders.add(orderOfCustomerB);

        Assertions.assertThatExceptionOfType(OrderNotBelongsToCustomerException.class)
                .isThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customerA.id().value(), orderOfCustomerB.id().toString()));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenOrderIsNotReady(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.PLACED)
                .build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(CantAddLoyaltyPointsOrderIsNotReady.class)
                .isThrownBy(() -> customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().toString()));
    }

    @Test
    public void shouldNotAddLoyaltyPointsWhenOrderAmountIsBelowThreshold(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .withItems(false)
                .status(OrderStatus.DRAFT)
                .build();
        order.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();
        orders.add(order);

        customerLoyaltyPointsApplicationService.addLoyaltyPoints(customer.id().value(), order.id().toString());

        Customer updatedCustomer = customers.ofId(customer.id()).orElseThrow();
        Assertions.assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
    }

}
