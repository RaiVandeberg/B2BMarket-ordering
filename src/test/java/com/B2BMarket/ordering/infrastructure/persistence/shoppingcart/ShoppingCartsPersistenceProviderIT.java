package com.B2BMarket.ordering.infrastructure.persistence.shoppingcart;

import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.B2BMarket.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.B2BMarket.ordering.domain.model.product.ProductTestDataBuilder.aProductAltMousePad;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartsPersistenceProviderIT {

    private final ShoppingCartsPersistenceProvider persistenceProvider;
    private final CustomersPersistenceProvider customersPersistenceProvider;
    private final ShoppingCartPersistenceEntityRepository entityRepository;

    @Autowired
    public ShoppingCartsPersistenceProviderIT(ShoppingCartsPersistenceProvider persistenceProvider,
                                              ShoppingCartPersistenceEntityRepository entityRepository,
                                              CustomersPersistenceProvider customersPersistenceProvider) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
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
    public void shouldPersistShoppingCartAndSaveAllFieldsCorrectly() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        ShoppingCartPersistenceEntity persistenceEntity = entityRepository.findById(cart.id().value()).orElseThrow();

        Assertions.assertThat(persistenceEntity.getCustomerId()).isEqualTo(cart.customerId().value());
        Assertions.assertThat(persistenceEntity.getTotalAmount()).isEqualByComparingTo(cart.totalAmount().value());
        Assertions.assertThat(persistenceEntity.getTotalItems()).isEqualTo(cart.totalItems().value());
        Assertions.assertThat(persistenceEntity.getItems()).hasSize(2);

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    public void shouldUpdateCartAndIncrementVersion() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        cart = persistenceProvider.ofId(cart.id()).orElseThrow();
        cart.addItem(aProductAltMousePad().build(), new Quantity(3));
        persistenceProvider.add(cart);

        ShoppingCartPersistenceEntity persistenceEntity = entityRepository.findById(cart.id().value()).orElseThrow();
        Assertions.assertThat(persistenceEntity.getItems()).hasSize(3);
        Assertions.assertThat(persistenceEntity.getVersion()).isGreaterThan(0);
    }

    @Test
    public void shouldFindCartByIdWithCompleteMapping() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        ShoppingCart found = persistenceProvider.ofId(cart.id()).orElseThrow();

        Assertions.assertThat(found.id()).isEqualTo(cart.id());
        Assertions.assertThat(found.customerId()).isEqualTo(cart.customerId());
        Assertions.assertThat(found.totalAmount()).isEqualTo(cart.totalAmount());
        Assertions.assertThat(found.totalItems()).isEqualTo(cart.totalItems());
        Assertions.assertThat(found.items()).hasSize(2);
    }

    @Test
    public void shouldFindCartByCustomerId() {
        CustomerId uniqueCustomerId = new CustomerId();
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .id(uniqueCustomerId).build();
        customersPersistenceProvider.add(customer);

        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(uniqueCustomerId).build();
        persistenceProvider.add(cart);

        ShoppingCart found = persistenceProvider.ofCustomer(uniqueCustomerId).orElseThrow();

        Assertions.assertThat(found.id()).isEqualTo(cart.id());
        Assertions.assertThat(found.customerId()).isEqualTo(uniqueCustomerId);
    }

    @Test
    public void shouldRemoveCart() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        Assertions.assertThat(persistenceProvider.exists(cart.id())).isTrue();

        persistenceProvider.remove(cart);

        Assertions.assertThat(persistenceProvider.exists(cart.id())).isFalse();
    }

    @Test
    public void shouldCountAndCheckExistence() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        Assertions.assertThat(persistenceProvider.count()).isGreaterThanOrEqualTo(1);
        Assertions.assertThat(persistenceProvider.exists(cart.id())).isTrue();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceProvider.add(cart);

        Assertions.assertThatNoException().isThrownBy(() -> {
            persistenceProvider.ofId(cart.id()).orElseThrow();
        });
    }
}
