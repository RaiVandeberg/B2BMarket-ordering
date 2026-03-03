package com.B2BMarket.ordering.domain.exception;

import com.B2BMarket.ordering.domain.valueObject.id.ProductId;

public class ProductOutOfStockException extends DomainException{

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK,id ));
    }
}
