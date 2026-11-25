package com.B2BMarket.ordering.infrastructure.persistence.shoppingcart;

import com.B2BMarket.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.B2BMarket.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.B2BMarket.ordering.application.utility.Mapper;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public ShoppingCartOutput findById(UUID shoppingCartId) {
        Objects.requireNonNull(shoppingCartId);
        ShoppingCartPersistenceEntity entity = repository.findById(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(new ShoppingCartId(shoppingCartId)));
        return mapper.convert(entity, ShoppingCartOutput.class);
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        Objects.requireNonNull(customerId);
        ShoppingCartPersistenceEntity entity = repository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(new CustomerId(customerId)));
        return mapper.convert(entity, ShoppingCartOutput.class);
    }
}
