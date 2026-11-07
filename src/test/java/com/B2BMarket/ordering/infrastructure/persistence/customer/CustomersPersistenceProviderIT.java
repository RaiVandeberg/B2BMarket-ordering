package com.B2BMarket.ordering.infrastructure.persistence.customer;

import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class CustomersPersistenceProviderIT {

    private final CustomersPersistenceProvider persistenceProvider;
    private final CustomerPersistenceEntityRepository entityRepository;

    @Autowired
    public CustomersPersistenceProviderIT(CustomersPersistenceProvider persistenceProvider,
                                          CustomerPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

    @Test
    public void shouldPersistCustomerAndSaveAllFieldsCorrectly() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        CustomerPersistenceEntity persistenceEntity = entityRepository.findById(customer.id().value()).orElseThrow();

        Assertions.assertThat(persistenceEntity.getFirstName()).isEqualTo("John");
        Assertions.assertThat(persistenceEntity.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(persistenceEntity.getEmail()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(persistenceEntity.getPhone()).isEqualTo("478-256-2604");
        Assertions.assertThat(persistenceEntity.getDocument()).isEqualTo("255-08-0578");
        Assertions.assertThat(persistenceEntity.getPromotionNotificationsAllowed()).isTrue();
        Assertions.assertThat(persistenceEntity.getArchived()).isFalse();
        Assertions.assertThat(persistenceEntity.getRegisteredAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLoyaltyPoints()).isEqualTo(0);
        Assertions.assertThat(persistenceEntity.getAddress()).isNotNull();
        Assertions.assertThat(persistenceEntity.getAddress().getStreet()).isEqualTo("Bourbon Street");

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    public void shouldUpdateCustomerAndIncrementVersion() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        customer = persistenceProvider.ofId(customer.id()).orElseThrow();
        customer.changeEmail(new Email("newemail@email.com"));
        persistenceProvider.add(customer);

        CustomerPersistenceEntity persistenceEntity = entityRepository.findById(customer.id().value()).orElseThrow();
        Assertions.assertThat(persistenceEntity.getEmail()).isEqualTo("newemail@email.com");
        Assertions.assertThat(persistenceEntity.getVersion()).isGreaterThan(0);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldThrowOptimisticLockingExceptionWhenVersionIsStale() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        Customer firstRead = persistenceProvider.ofId(customer.id()).orElseThrow();
        Customer secondRead = persistenceProvider.ofId(customer.id()).orElseThrow();

        firstRead.changeEmail(new Email("first@email.com"));
        persistenceProvider.add(firstRead);

        secondRead.changeEmail(new Email("second@email.com"));
        Assertions.assertThatThrownBy(() -> persistenceProvider.add(secondRead))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void shouldFindCustomerByIdWithCompleteMapping() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        Customer found = persistenceProvider.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(found.id()).isEqualTo(customer.id());
        Assertions.assertThat(found.fullName().firstName()).isEqualTo("John");
        Assertions.assertThat(found.fullName().lastName()).isEqualTo("Doe");
        Assertions.assertThat(found.email().value()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(found.phone().value()).isEqualTo("478-256-2604");
        Assertions.assertThat(found.document().value()).isEqualTo("255-08-0578");
        Assertions.assertThat(found.isPromotionNotificationsAllowed()).isTrue();
        Assertions.assertThat(found.isArchived()).isFalse();
        Assertions.assertThat(found.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
        Assertions.assertThat(found.address().street()).isEqualTo("Bourbon Street");
        Assertions.assertThat(found.address().zipCode().value()).isEqualTo("12345");
    }

    @Test
    public void shouldCountAndCheckExistence() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        Assertions.assertThat(persistenceProvider.count()).isGreaterThanOrEqualTo(1);
        Assertions.assertThat(persistenceProvider.exists(customer.id())).isTrue();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        Assertions.assertThatNoException().isThrownBy(() -> {
            persistenceProvider.ofId(customer.id()).orElseThrow();
        });
    }
}
