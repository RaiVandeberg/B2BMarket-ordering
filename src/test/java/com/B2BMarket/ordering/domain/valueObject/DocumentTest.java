package com.B2BMarket.ordering.domain.valueObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldCreateDocumentWithValidValue() {
        Document document = new Document("12345678900");
        assertNotNull(document);
        assertEquals("12345678900", document.value());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(NullPointerException.class, () -> new Document(null));
    }

    @Test
    void shouldThrowExceptionForBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new Document(" "));
        assertThrows(IllegalArgumentException.class, () -> new Document(""));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        String value = "123.456.789-00";
        Document document = new Document(value);
        assertEquals(value, document.toString());
    }
}