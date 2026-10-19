package com.B2BMarket.ordering.infrastructure.persistence.assembler;

import com.B2BMarket.ordering.domain.model.entity.Customer;
import com.B2BMarket.ordering.domain.model.valueObject.Address;
import com.B2BMarket.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.B2BMarket.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity persistenceEntity, Customer customer) {
        persistenceEntity.setId(customer.id().value());
        persistenceEntity.setFirstName(customer.fullName().firstName());
        persistenceEntity.setLastName(customer.fullName().lastName());
        persistenceEntity.setBirthDate(customer.birthDate() != null ? customer.birthDate().birthDate() : null);
        persistenceEntity.setEmail(customer.email().value());
        persistenceEntity.setPhone(customer.phone().value());
        persistenceEntity.setDocument(customer.document().value());
        persistenceEntity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        persistenceEntity.setArchived(customer.isArchived());
        persistenceEntity.setRegisteredAt(customer.registeredAt());
        persistenceEntity.setArchivedAt(customer.archivedAt());
        persistenceEntity.setLoyaltyPoints(customer.loyaltyPoints().value());
        persistenceEntity.setAddress(assembleAddress(customer.address()));
        persistenceEntity.setVersion(customer.version());
        return persistenceEntity;
    }

    private AddressEmbeddable assembleAddress(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode().value())
                .build();
    }
}
