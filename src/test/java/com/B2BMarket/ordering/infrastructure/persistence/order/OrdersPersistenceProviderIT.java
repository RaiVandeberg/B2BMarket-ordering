package com.B2BMarket.ordering.infrastructure.persistence.order;

import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.OrderStatus;
import com.B2BMarket.ordering.domain.model.order.OrderTestDataBuilder;
import com.B2BMarket.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.B2BMarket.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceProviderIT {

    private OrdersPersistenceProvider persistenceProvider;
    private CustomersPersistenceProvider customersPersistenceProvider;
    private OrderPersistenceEntityRepository entityRepository;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider persistenceProvider,
                                       OrderPersistenceEntityRepository persistenceEntityRepository,
                                       CustomersPersistenceProvider customersPersistenceProvider) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = persistenceEntityRepository;
        this.customersPersistenceProvider = customersPersistenceProvider;
    }

    @BeforeEach
    public void setUp() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    public void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();
        persistenceProvider.add(order);

        var persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceProvider.add(order);

        persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction() {
        Order order = OrderTestDataBuilder.anOrder().build();
        persistenceProvider.add(order);

        Assertions.assertThatNoException().isThrownBy(() -> {
            persistenceProvider.ofId(order.id()).orElseThrow();
        });

    }
}