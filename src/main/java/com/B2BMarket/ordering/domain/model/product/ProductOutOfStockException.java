package com.B2BMarket.ordering.domain.model.product;
import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.ErrorMessages;


public class ProductOutOfStockException extends DomainException{

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK,id ));
    }
}
