package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ReadProductFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProductLoaderTest {

    private static final String VALID_JSON = "[{\"id\":1,\"name\":\"Product 1\",\"price\":10.0}]";
    private static final String INVALID_JSON = "[{\"id\":1,\"name\":\"Product 1\"";

    @Test
    @DisplayName("loadProducts returns list when JSON is valid")
    void loadProducts_ReturnsListWhenValidJson() {
        try (MockedStatic<ProductLoader> mocked = Mockito.mockStatic(ProductLoader.class, Mockito.CALLS_REAL_METHODS)) {
            // Mock getResourceAsStream para retornar stream com JSON válido
            mocked.when(() -> ProductLoader.class.getResourceAsStream("/products.json"))
                    .thenReturn(new ByteArrayInputStream(VALID_JSON.getBytes(StandardCharsets.UTF_8)));

            List<Product> products = ProductLoader.loadProducts();

            assertThat(products).isNotNull();
            assertThat(products).hasSize(1);
            assertThat(products.get(0).getName()).isEqualTo("Product 1");
        }
    }

    @Test
    @DisplayName("loadProducts throws IllegalStateException when file not found")
    void loadProducts_ThrowsWhenFileNotFound() {
        try (MockedStatic<ProductLoader> mocked = Mockito.mockStatic(ProductLoader.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(() -> ProductLoader.class.getResourceAsStream("/products.json"))
                    .thenReturn(null);

            assertThatThrownBy(ProductLoader::loadProducts)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("products.json file not found");
        }
    }

    @Test
    @DisplayName("loadProducts throws ReadProductFileException for invalid JSON")
    void loadProducts_ThrowsReadProductFileExceptionForInvalidJson() {
        try (MockedStatic<ProductLoader> mocked = Mockito.mockStatic(ProductLoader.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(() -> ProductLoader.class.getResourceAsStream("/products.json"))
                    .thenReturn(new ByteArrayInputStream(INVALID_JSON.getBytes(StandardCharsets.UTF_8)));

            assertThatThrownBy(ProductLoader::loadProducts)
                    .isInstanceOf(ReadProductFileException.class)
                    .hasMessageContaining("Invalid JSON format");
        }
    }

    // Para IOException, simular InputStream que lança IOException é mais complexo,
    // pode ser ignorado ou feito com InputStream customizado lançando IOException no read()

}
