package com.B2BMarket.ordering.domain.valueObject;

import com.B2BMarket.ordering.domain.validator.FieldValidations;
import lombok.Builder;
import org.apache.tomcat.util.json.JSONParser;

import java.util.Objects;

public record Address(
        String street,
        String complement,
        String neighborhood,
        String number,
        String city,
        String state,
        ZipCode zipCode
) {

    @Builder(toBuilder = true)
    public Address {
        FieldValidations.requireNonBlank(street);
        FieldValidations.requireNonBlank(neighborhood);
        FieldValidations.requireNonBlank(number);
        FieldValidations.requireNonBlank(city);
        FieldValidations.requireNonBlank(state);
        Objects.requireNonNull(zipCode);
    }

}
