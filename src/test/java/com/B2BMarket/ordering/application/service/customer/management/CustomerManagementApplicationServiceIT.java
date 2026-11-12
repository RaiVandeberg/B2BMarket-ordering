package com.B2BMarket.ordering.application.service.customer.management;


import com.B2BMarket.ordering.application.customer.management.CustomerManagementApplicationService;
import com.B2BMarket.ordering.application.commons.AddressData;
import com.B2BMarket.ordering.application.customer.management.CustomerInput;
import com.B2BMarket.ordering.application.customer.management.CustomerOutput;
import com.B2BMarket.ordering.application.customer.management.CustomerUpdateInput;
import com.B2BMarket.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.B2BMarket.ordering.domain.model.customer.*;
import com.B2BMarket.ordering.infrastructure.listerner.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationApplicationService customerNotificationApplicationService;


    @Test
    public void shouldRegister(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput)
            .extracting(
                    CustomerOutput::getId,
                    CustomerOutput::getFirstName,
                    CustomerOutput::getEmail,
                    CustomerOutput::getBirthDate,
                    CustomerOutput::getPhone,
                    CustomerOutput::getDocument
            )
            .containsExactly(
                    customerId,
                    "John",
                    "johndoe@email.com",
                    LocalDate.of(1991, 7, 5),
                    "478-256-2604",
                    "255-08-0578"
            );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerEventListener, Mockito.never())
                .listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationApplicationService).notifyNewRegistration(Mockito.any(CustomerNotificationApplicationService.NotifyNewRegistrationInput.class));
    }

    @Test
    public void shouldUpdate(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument
                )
                .containsExactly(
                        customerId,
                        "Jane",
                        "Smith",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7, 5),
                        "478-256-2605",
                        "255-08-0578"
                );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    public void shouldArchive(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput.getArchived()).isTrue();
        Assertions.assertThat(customerOutput.getArchivedAt()).isNotNull();

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                )
                .containsExactly(
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(customerOutput.getEmail()).endsWith("@anonymous.com");

        AddressData address = customerOutput.getAddress();
        Assertions.assertThat(address.getNumber()).isEqualTo("Anonymized");
        Assertions.assertThat(address.getComplement()).isNull();
    }

    @Test
    public void shouldNotArchiveWhenCustomerNotFound(){
        UUID nonExistentCustomerId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(nonExistentCustomerId));
    }

    @Test
    public void shouldNotArchiveWhenCustomerAlreadyArchived(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
    public void shouldChangeEmail(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.changeEmail(customerId, "newemail@email.com");

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("newemail@email.com");
    }

    @Test
    public void shouldNotChangeEmailWhenCustomerNotFound(){
        UUID nonExistentCustomerId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(nonExistentCustomerId, "newemail@email.com"));
    }

    @Test
    public void shouldNotChangeEmailWhenCustomerIsArchived(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "newemail@email.com"));
    }

    @Test
    public void shouldNotChangeEmailWhenFormatIsInvalid(){
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "email-invalido"));
    }

    @Test
    public void shouldNotChangeEmailWhenAlreadyInUse(){
        CustomerInput firstInput = CustomerInputTestDataBuilder.aCustomer().build();
        UUID firstCustomerId = customerManagementApplicationService.create(firstInput);

        CustomerInput secondInput = CustomerInputTestDataBuilder.aCustomer()
                .email("janedoe@email.com")
                .build();
        customerManagementApplicationService.create(secondInput);

        Assertions.assertThatExceptionOfType(CustomerEmailIsUseException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(firstCustomerId, "janedoe@email.com"));
    }

}