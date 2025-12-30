package com.B2BMarket.ordering.domain.valueObject;


import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.B2BMarket.ordering.domain.validator.FieldValidations.requiresDateInPast;


public record BirthDate(LocalDate birthDate) {

    public BirthDate(LocalDate birthDate){
        Objects.requireNonNull(birthDate);
        requiresDateInPast(birthDate);

        this.birthDate = birthDate;
    }

    public Integer age() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return birthDate.toString();
    }
}
