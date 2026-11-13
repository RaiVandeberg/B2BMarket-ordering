package com.B2BMarket.ordering.infrastructure.listerner.customer;

import com.B2BMarket.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderReadyEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class CustomerEventListenerIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoBean
    private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @MockitoSpyBean
    private CustomerNotificationApplicationService customerNotificationApplicationService;

    @Test
    public void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(new OrderReadyEvent(
                new OrderId(),
                new CustomerId(),
                OffsetDateTime.now()
                )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(OrderReadyEvent.class));

        Mockito.verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(Mockito.any(UUID.class), Mockito.any(String.class));
    }

    @Test
    public void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(new CustomerRegisteredEvent(
                new CustomerId(),
                OffsetDateTime.now(),
                new FullName("John", "Doe"),
                new Email("john.doe@gmail.com")
            )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerNotificationApplicationService).notifyNewRegistration(Mockito.any(CustomerNotificationApplicationService.NotifyNewRegistrationInput.class));
    }


}