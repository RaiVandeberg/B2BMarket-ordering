package com.B2BMarket.ordering.infrastructure.notification.customer;

import com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationApplicationServiceFakeImpl implements CustomerNotificationApplicationService {

    @Override
    public void notifyNewRegistration(NotifyNewRegistrationInput input) {

        log.info("Welcome {}", input.firstName());
        log.info("User your email to access your account {}", input.email());
    }
}
