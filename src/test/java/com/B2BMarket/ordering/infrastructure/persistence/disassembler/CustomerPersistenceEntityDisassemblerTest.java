package com.B2BMarket.ordering.infrastructure.persistence.disassembler;

import com.B2BMarket.ordering.domain.model.commons.Document;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Phone;
import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.customer.BirthDate;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.LoyaltyPoints;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerPersistenceEntityDisassemblerTest {

    private final CustomerPersistenceEntityDisassembler disassembler = new CustomerPersistenceEntityDisassembler();

    @Test
    public void shouldConvertFromPersistence() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.existingCustomer().build();
        Customer domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new CustomerId(persistenceEntity.getId())),
                s -> assertThat(s.fullName()).isEqualTo(new FullName(persistenceEntity.getFirstName(), persistenceEntity.getLastName())),
                s -> assertThat(s.birthDate()).isEqualTo(new BirthDate(persistenceEntity.getBirthDate())),
                s -> assertThat(s.email()).isEqualTo(new Email(persistenceEntity.getEmail())),
                s -> assertThat(s.phone()).isEqualTo(new Phone(persistenceEntity.getPhone())),
                s -> assertThat(s.document()).isEqualTo(new Document(persistenceEntity.getDocument())),
                s -> assertThat(s.isPromotionNotificationsAllowed()).isEqualTo(persistenceEntity.getPromotionNotificationsAllowed()),
                s -> assertThat(s.isArchived()).isEqualTo(persistenceEntity.getArchived()),
                s -> assertThat(s.registeredAt()).isEqualTo(persistenceEntity.getRegisteredAt()),
                s -> assertThat(s.archivedAt()).isEqualTo(persistenceEntity.getArchivedAt()),
                s -> assertThat(s.loyaltyPoints()).isEqualTo(new LoyaltyPoints(persistenceEntity.getLoyaltyPoints()))
        );
    }

    @Test
    public void shouldConvertAddressFromPersistence() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.existingCustomer().build();
        Customer domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.address()).satisfies(
                a -> assertThat(a.street()).isEqualTo(persistenceEntity.getAddress().getStreet()),
                a -> assertThat(a.number()).isEqualTo(persistenceEntity.getAddress().getNumber()),
                a -> assertThat(a.complement()).isEqualTo(persistenceEntity.getAddress().getComplement()),
                a -> assertThat(a.neighborhood()).isEqualTo(persistenceEntity.getAddress().getNeighborhood()),
                a -> assertThat(a.city()).isEqualTo(persistenceEntity.getAddress().getCity()),
                a -> assertThat(a.state()).isEqualTo(persistenceEntity.getAddress().getState()),
                a -> assertThat(a.zipCode()).isEqualTo(new ZipCode(persistenceEntity.getAddress().getZipCode()))
        );
    }

    @Test
    public void shouldHandleNullBirthDate() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.existingCustomer()
                .birthDate(null)
                .build();

        Customer domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.birthDate()).isNull();
    }

    @Test
    public void shouldHandleNullArchivedAt() {
        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.existingCustomer()
                .archivedAt(null)
                .build();

        Customer domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity.archivedAt()).isNull();
    }
}
