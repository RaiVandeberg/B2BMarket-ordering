package com.B2BMarket.ordering.domain.model.product;
import com.B2BMarket.ordering.domain.model.DomainException;
import com.B2BMarket.ordering.domain.model.ErrorMessages;


public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, id));
    }
}
