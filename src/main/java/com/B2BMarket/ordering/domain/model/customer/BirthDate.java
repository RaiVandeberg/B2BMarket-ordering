package com.B2BMarket.ordering.domain.model.customer;


import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.B2BMarket.ordering.domain.model.FieldValidations.requiresDateInPast;


public record BirthDate(LocalDate value) {

    public BirthDate(LocalDate value){
        Objects.requireNonNull(value);
        requiresDateInPast(value);

        this.value = value;
    }

    public Integer age() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
