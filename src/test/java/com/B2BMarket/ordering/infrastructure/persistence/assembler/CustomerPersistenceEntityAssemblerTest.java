package com.B2BMarket.ordering.infrastructure.persistence.assembler;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.valueObject.Email;
import com.B2BMarket.ordering.domain.model.valueObject.FullName;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerPersistenceEntityAssemblerTest {

    private final CustomerPersistenceEntityAssembler assembler = new CustomerPersistenceEntityAssembler();

    @Test
    void shouldConvertFromDomain() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(customer);

        assertThat(persistenceEntity).satisfies(
                p -> assertThat(p.getId()).isEqualTo(customer.id().value()),
                p -> assertThat(p.getFirstName()).isEqualTo(customer.fullName().firstName()),
                p -> assertThat(p.getLastName()).isEqualTo(customer.fullName().lastName()),
                p -> assertThat(p.getBirthDate()).isEqualTo(customer.birthDate().birthDate()),
                p -> assertThat(p.getEmail()).isEqualTo(customer.email().value()),
                p -> assertThat(p.getPhone()).isEqualTo(customer.phone().value()),
                p -> assertThat(p.getDocument()).isEqualTo(customer.document().value()),
                p -> assertThat(p.getPromotionNotificationsAllowed()).isEqualTo(customer.isPromotionNotificationsAllowed()),
                p -> assertThat(p.getArchived()).isEqualTo(customer.isArchived()),
                p -> assertThat(p.getRegisteredAt()).isEqualTo(customer.registeredAt()),
                p -> assertThat(p.getLoyaltyPoints()).isEqualTo(customer.loyaltyPoints().value())
        );
    }

    @Test
    void shouldConvertAddress() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(customer);

        assertThat(persistenceEntity.getAddress()).satisfies(
                a -> assertThat(a.getStreet()).isEqualTo(customer.address().street()),
                a -> assertThat(a.getNumber()).isEqualTo(customer.address().number()),
                a -> assertThat(a.getComplement()).isEqualTo(customer.address().complement()),
                a -> assertThat(a.getNeighborhood()).isEqualTo(customer.address().neighborhood()),
                a -> assertThat(a.getCity()).isEqualTo(customer.address().city()),
                a -> assertThat(a.getState()).isEqualTo(customer.address().state()),
                a -> assertThat(a.getZipCode()).isEqualTo(customer.address().zipCode().value())
        );
    }

    @Test
    void shouldMergeUpdatedFieldsIntoPersistenceEntity() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.existingCustomer().build();

        customer.changeEmail(new Email("updated@email.com"));
        customer.changeName(new FullName("Jane", "Smith"));

        assembler.merge(persistenceEntity, customer);

        assertThat(persistenceEntity.getEmail()).isEqualTo("updated@email.com");
        assertThat(persistenceEntity.getFirstName()).isEqualTo("Jane");
        assertThat(persistenceEntity.getLastName()).isEqualTo("Smith");
    }

    @Test
    void shouldHandleNullBirthDate() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(customer);

        assertThat(persistenceEntity.getBirthDate()).isNull();
    }
}
