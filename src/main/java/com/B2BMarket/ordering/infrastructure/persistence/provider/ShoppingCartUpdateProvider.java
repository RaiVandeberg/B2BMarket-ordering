package com.B2BMarket.ordering.infrastructure.persistence.provider;

import com.B2BMarket.ordering.domain.model.service.ShoppingCartProductAdjustmentService;
import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;


    @Override
    @Transactional
    public void addProduct(Product productId, Money updatedPrice) {
        shoppingCartPersistenceEntityRepository.updateItemPrice(productId.id().value(), updatedPrice.value());
        shoppingCartPersistenceEntityRepository.recalculateTotalsForCartsWithProduct(productId.id().value());
    }

    @Override
    @Transactional
    public void changeAvailability(Product productId, boolean available) {
        shoppingCartPersistenceEntityRepository.updateItemAvailability(productId.id().value(), available);
    }
}
