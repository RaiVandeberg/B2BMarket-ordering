package com.B2BMarket.ordering.domain.model.commons;

import static com.B2BMarket.ordering.domain.model.FieldValidations.requiresValidEmail;

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