package com.B2BMarket.ordering.domain.valueObject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNameTest {

    @Test
    void given_validName_whenCreate_shouldSucceed() {
        ProductName name = new ProductName("Notebook");

        assertThat(name.toString()).isEqualTo("Notebook");
    }

    @Test
    void given_blankName_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ProductName("   "));
    }

    @Test
    void given_nullName_whenCreate_shouldThrowException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ProductName(null));
    }
}
