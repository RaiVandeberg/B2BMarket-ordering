package com.B2BMarket.ordering.domain.model.valueObject;

import static com.B2BMarket.ordering.domain.model.validator.FieldValidations.requiresValidEmail;

public record Email(String value) {

    public Email(String value){
        requiresValidEmail(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}