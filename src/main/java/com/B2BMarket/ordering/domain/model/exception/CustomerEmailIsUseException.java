package com.B2BMarket.ordering.domain.model.exception;

import static com.B2BMarket.ordering.domain.model.exception.ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE;

public class CustomerEmailIsUseException extends DomainException {

    public CustomerEmailIsUseException() {
        super(ERROR_CUSTOMER_EMAIL_IS_IN_USE);
    }
}
