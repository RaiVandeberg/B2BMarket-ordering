package com.B2BMarket.ordering.domain.valueObject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class BirthDateTest {

    @Test
    void shouldCreateBirthDateWithValidDate() {
        LocalDate pastDate = LocalDate.now().minusYears(25);
        BirthDate birthDate = new BirthDate(pastDate);
        assertNotNull(birthDate);
        assertEquals(pastDate, birthDate.birthDate());
    }

    @Test
    void shouldThrowExceptionForNullDate() {
        assertThrows(NullPointerException.class, () -> new BirthDate(null));
    }

    @Test
    void shouldThrowExceptionForFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> new BirthDate(futureDate));
    }

    @Test
    void shouldCalculateAgeCorrectly() {
        LocalDate birthDate = LocalDate.now().minusYears(30).minusDays(1);
        BirthDate bd = new BirthDate(birthDate);
        assertEquals(30, bd.age());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        LocalDate date = LocalDate.of(1990, 5, 15);
        BirthDate birthDate = new BirthDate(date);
        assertEquals("1990-05-15", birthDate.toString());
    }
}