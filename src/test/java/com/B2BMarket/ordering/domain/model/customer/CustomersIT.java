package com.B2BMarket.ordering.domain.model.customer;

import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
})
class CustomersIT {

    private Customers customers;

    @Autowired
    public CustomersIT(Customers customers) {
        this.customers = customers;
    }

    @Test
    public void shouldPersistAndFind() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerId customerId = customer.id();
        customers.add(customer);

        Optional<Customer> possibleCustomer = customers.ofId(customerId);

        assertThat(possibleCustomer).isPresent();

        Customer savedCustomer = possibleCustomer.get();

        assertThat(savedCustomer).satisfies(
                s -> assertThat(s.id()).isEqualTo(customerId),
                s -> assertThat(s.fullName().firstName()).isEqualTo("John"),
                s -> assertThat(s.fullName().lastName()).isEqualTo("Doe"),
                s -> assertThat(s.email().value()).isEqualTo("johndoe@email.com"),
                s -> assertThat(s.phone().value()).isEqualTo("478-256-2604"),
                s -> assertThat(s.document().value()).isEqualTo("255-08-0578"),
                s -> assertThat(s.isPromotionNotificationsAllowed()).isTrue(),
                s -> assertThat(s.isArchived()).isFalse(),
                s -> assertThat(s.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO),
                s -> assertThat(s.address().street()).isEqualTo("Bourbon Street")
        );
    }

    @Test
    public void shouldUpdateExistingCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        customer = customers.ofId(customer.id()).orElseThrow();
        customer.changeEmail(new Email("newemail@email.com"));
        customers.add(customer);

        customer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(customer.email().value()).isEqualTo("newemail@email.com");
    }

    @Test
    public void shouldNotAllowStaleUpdates() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Customer customerT1 = customers.ofId(customer.id()).orElseThrow();
        Customer customerT2 = customers.ofId(customer.id()).orElseThrow();

        customerT1.changeEmail(new Email("first@email.com"));
        customers.add(customerT1);

        customerT2.changeEmail(new Email("second@email.com"));

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> customers.add(customerT2));

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(savedCustomer.email().value()).isEqualTo("first@email.com");
    }

    @Test
    public void shouldCountExistingCustomers() {
        Assertions.assertThat(customers.count()).isZero();

        Customer customer1 = CustomerTestDataBuilder.brandNewCustomer().build();
        Customer customer2 = CustomerTestDataBuilder.brandNewCustomer().email(new Email("second@email.com")).build();

        customers.add(customer1);
        customers.add(customer2);

        Assertions.assertThat(customers.count()).isEqualTo(2L);
    }

    @Test
    public void shouldReturnIfCustomerExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.exists(customer.id())).isTrue();
        Assertions.assertThat(customers.exists(new CustomerId())).isFalse();
    }

    @Test
    public void shouldFindCustomerByEmail() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Optional<Customer> found = customers.ofEmail(new Email("johndoe@email.com"));

        assertThat(found).isPresent();
        assertThat(found.get().id()).isEqualTo(customer.id());
    }

    @Test
    public void shouldReturnEmptyWhenEmailNotFound() {
        Optional<Customer> found = customers.ofEmail(new Email("nonexistent@email.com"));

        assertThat(found).isNotPresent();
    }

    @Test
    public void shouldReturnIfEmailIsInUse(){
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.isEmailUnique(customer.email(), customer.id())).isTrue();
        Assertions.assertThat(customers.isEmailUnique(customer.email(),new CustomerId())).isFalse();
        Assertions.assertThat(customers.isEmailUnique(new Email("rai@gmail.com"),new CustomerId())).isTrue();

    }
}
