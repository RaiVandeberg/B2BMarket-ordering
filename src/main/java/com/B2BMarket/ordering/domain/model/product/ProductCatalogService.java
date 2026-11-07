package com.B2BMarket.ordering.domain.model.product;


import java.util.Optional;

public interface ProductCatalogService {
    Optional<Product> ofId(ProductId productId);
}
