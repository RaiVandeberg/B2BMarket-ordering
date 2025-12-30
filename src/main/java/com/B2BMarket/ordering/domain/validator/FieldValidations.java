package com.B2BMarket.ordering.domain.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.util.Objects;

import static com.B2BMarket.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST;

public class FieldValidations {
    private FieldValidations(){

    }


    public static void requiresValidEmail(String email){
        requiresValidEmail(email, null);
    }

    public static void requiresValidEmail(String email, String errorMessage){
        Objects.requireNonNull(email, errorMessage);
        if(email.isBlank()){
            throw new IllegalArgumentException(errorMessage);

        }
        if(!EmailValidator.getInstance().isValid(email)){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requiresDateInPast(LocalDate date) {
        requiresDateInPast(date, VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
    }

    public static void requiresDateInPast(LocalDate date, String errorMessage) {
        Objects.requireNonNull(date, errorMessage);
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
