package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ReadProductFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProductLoaderTest {

    @Test
    @DisplayName("Should load products successfully when the file exists and is valid")
    void shouldLoadProductsSuccessfully() {
        List<Product> products = ProductLoader.loadProducts();

        assertThat(products).hasSize(100);
        assertThat(products.getFirst().getName()).isEqualTo("Smartphone X200");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when the file is not found")
    void shouldThrowWhenFileNotFound() {
        try (MockedStatic<ProductLoader> mocked = mockStatic(ProductLoader.class, CALLS_REAL_METHODS)) {
            mocked.when(ProductLoader::getProductStream).thenReturn(null);

            assertThatThrownBy(ProductLoader::loadProducts)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("products.json file not found");
        }
    }

    @Test
    @DisplayName("Should throw ReadProductFileException when the JSON format is invalid")
    void shouldThrowWhenJsonIsInvalid() {
        String invalidJson = "{ invalid json ]";
        InputStream invalidStream = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));

        try (MockedStatic<ProductLoader> mocked = mockStatic(ProductLoader.class, CALLS_REAL_METHODS)) {
            mocked.when(ProductLoader::getProductStream).thenReturn(invalidStream);

            assertThatThrownBy(ProductLoader::loadProducts)
                    .isInstanceOf(ReadProductFileException.class)
                    .hasMessageContaining("Invalid JSON format");
        }
    }

    @Test
    @DisplayName("Should throw ReadProductFileException when an IOException occurs")
    void shouldThrowWhenIOExceptionOccurs() throws IOException {
        InputStream faultyStream = mock(InputStream.class);

        when(faultyStream.read(any(byte[].class), anyInt(), anyInt()))
                .thenThrow(new IOException("Simulated IO error"));

        try (MockedStatic<ProductLoader> mocked = mockStatic(ProductLoader.class, CALLS_REAL_METHODS)) {
            mocked.when(ProductLoader::getProductStream).thenReturn(faultyStream);

            assertThatThrownBy(ProductLoader::loadProducts)
                    .isInstanceOf(ReadProductFileException.class)
                    .hasMessageContaining("Error reading products.json");
        }
    }
}
