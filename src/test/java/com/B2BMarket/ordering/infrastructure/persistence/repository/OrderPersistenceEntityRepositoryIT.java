package com.B2BMarket.ordering.infrastructure.persistence.repository;

import com.B2BMarket.ordering.domain.model.utility.IdGenerator;
import com.B2BMarket.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //nao substituir o banco ja selecionado
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

    @Autowired
    public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository orderPersistenceEntityRepository) {
        this.orderPersistenceEntityRepository = orderPersistenceEntityRepository;
    }

    @Test
    public  void shouldPersist(){
        long orderId = IdGenerator.generateTSID().toLong();
        OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
                .id(orderId)
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(1000))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .build();

        orderPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(orderPersistenceEntityRepository.existsById(orderId)).isTrue();
    }

    @Test
    public  void shouldCount(){
        long orderCount = orderPersistenceEntityRepository.count();

        Assertions.assertThat(orderCount).isZero();

    }



}