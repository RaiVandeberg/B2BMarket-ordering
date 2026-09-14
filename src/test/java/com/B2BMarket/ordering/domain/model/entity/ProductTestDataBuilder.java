package com.B2BMarket.ordering.domain.model.entity;


import com.B2BMarket.ordering.domain.model.valueObject.Money;
import com.B2BMarket.ordering.domain.model.valueObject.Product;
import com.B2BMarket.ordering.domain.model.valueObject.ProductName;
import com.B2BMarket.ordering.domain.model.valueObject.id.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId PRODUCT_ID_NOTEBOOK = new ProductId();
    public static final ProductId PRODUCT_ID_DESKTOP = new ProductId();
    public static final ProductId PRODUCT_ID_RAM_MEMORY = new ProductId();
    public static final ProductId PRODUCT_ID_MOUSE_PAD = new ProductId();

    private ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(PRODUCT_ID_NOTEBOOK)
                .inStock(true)
                .name(new ProductName("Notebook X11"))
                .price(new Money("3000"));
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(PRODUCT_ID_DESKTOP)
                .name(new ProductName("Desktop FX9000"))
                .price(new Money("5000"))
                .inStock(false);
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
                .id(PRODUCT_ID_RAM_MEMORY)
                .name(new ProductName("4GB RAM"))
                .price(new Money("200"))
                .inStock(true);
    }

    public static Product.ProductBuilder aProductAltMousePad() {
        return Product.builder()
                .id(PRODUCT_ID_MOUSE_PAD)
                .name(new ProductName("Mouse Pad"))
                .price(new Money("100"))
                .inStock(true);
    }

}
