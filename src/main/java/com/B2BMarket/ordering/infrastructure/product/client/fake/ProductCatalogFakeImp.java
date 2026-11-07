package com.B2BMarket.ordering.infrastructure.product.client.fake;

import com.B2BMarket.ordering.domain.model.commons.Money;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductCatalogService;
import com.B2BMarket.ordering.domain.model.product.ProductId;
import com.B2BMarket.ordering.domain.model.product.ProductName;

import java.util.Optional;

public class ProductCatalogFakeImp implements ProductCatalogService {
    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder().id(productId)
                .inStock(true)
                .name(new ProductName("Notebook"))
                .price(new Money("3000"))
                .build();
        return Optional.of(product);
    }
}
