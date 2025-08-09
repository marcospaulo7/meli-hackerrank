package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ProductFilterUtilsTest {

    private static Product createProductWithField(String fieldName, Object value) {
        // Como Product provavelmente tem vários campos privados, vamos criar um objeto "fake" com os campos necessários.
        // Aqui assumo que Product tem setters ou construtores públicos, mas se não tiver, você deve ajustar ou usar reflection.
        // Para este exemplo, vou assumir setters públicos para simplificar:
        Product product = new Product();
        switch (fieldName) {
            case "name" -> product.setName((String) value);
            case "classification" -> product.setClassification((String) value);
            case "price" -> product.setPrice((BigDecimal) value);
            case "description" -> product.setDescription((String) value);
            case "id" -> product.setId((Long) value);
            // Adicione mais campos se precisar
        }
        if (product.getId() == null) {
            product.setId(1L); // id default para logs
        }
        return product;
    }

    @Test
    @DisplayName("matchesFilter returns true when string field matches (case insensitive)")
    void matchesFilter_ReturnsTrueForStringMatch() {
        Product product = createProductWithField("name", "ExampleName");
        boolean result = ProductFilterUtils.matchesFilter(product, "name", "examplename");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("matchesFilter returns false when string field does not match")
    void matchesFilter_ReturnsFalseForStringNoMatch() {
        Product product = createProductWithField("name", "ExampleName");
        boolean result = ProductFilterUtils.matchesFilter(product, "name", "otherName");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("matchesFilter returns false when field is null")
    void matchesFilter_ReturnsFalseWhenFieldIsNull() {
        Product product = createProductWithField("description", null);
        boolean result = ProductFilterUtils.matchesFilter(product, "description", "anything");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("matchesFilter returns false for non-existing field")
    void matchesFilter_ReturnsFalseForNoSuchField() {
        Product product = createProductWithField("name", "something");
        boolean result = ProductFilterUtils.matchesFilter(product, "nonExistingField", "value");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("matchesFilter compares BigDecimal fields correctly")
    void matchesFilter_ReturnsTrueForMatchingBigDecimal() {
        Product product = createProductWithField("price", new BigDecimal("10.50"));
        boolean result = ProductFilterUtils.matchesFilter(product, "price", "10.50");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("matchesFilter returns false for non-matching BigDecimal")
    void matchesFilter_ReturnsFalseForNonMatchingBigDecimal() {
        Product product = createProductWithField("price", new BigDecimal("10.50"));
        boolean result = ProductFilterUtils.matchesFilter(product, "price", "20.00");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("matchesFilter returns false for invalid BigDecimal filter value")
    void matchesFilter_ReturnsFalseForInvalidBigDecimalValue() {
        Product product = createProductWithField("price", new BigDecimal("10.50"));
        boolean result = ProductFilterUtils.matchesFilter(product, "price", "invalidNumber");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("compareBigDecimal returns true for matching values")
    void compareBigDecimal_ReturnsTrueForMatchingValues() {
        boolean result = ProductFilterUtils.compareBigDecimal("5.0", new BigDecimal("5.0"));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("compareBigDecimal returns false for different values")
    void compareBigDecimal_ReturnsFalseForDifferentValues() {
        boolean result = ProductFilterUtils.compareBigDecimal("5.0", new BigDecimal("10.0"));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("compareBigDecimal returns false for invalid input")
    void compareBigDecimal_ReturnsFalseForInvalidInput() {
        boolean result = ProductFilterUtils.compareBigDecimal("abc", new BigDecimal("10.0"));
        assertThat(result).isFalse();
    }
}
