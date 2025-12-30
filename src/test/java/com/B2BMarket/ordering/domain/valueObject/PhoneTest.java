package com.B2BMarket.ordering.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void shouldCreatePhoneWithValidValue() {
        Phone phone = new Phone("11987654321");
        assertNotNull(phone);
        assertEquals("11987654321", phone.value());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    void shouldThrowExceptionForBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new Phone(""));
        assertThrows(IllegalArgumentException.class, () -> new Phone(" "));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        String value = "(11) 98765-4321";
        Phone phone = new Phone(value);
        assertEquals(value, phone.toString());
    }
}