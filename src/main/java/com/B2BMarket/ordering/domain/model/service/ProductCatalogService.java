package com.B2BMarket.ordering.domain.model.service;

import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;

import java.util.Optional;

public interface ProductCatalogService {
    Optional<Product> ofId(ProductId productId);
}
