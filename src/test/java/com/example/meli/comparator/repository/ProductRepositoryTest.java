package com.example.meli.comparator.repository;


import com.example.meli.comparator.data.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class ProductRepositoryTest {

    @InjectMocks
    private ProductRepository repository;

    @Test
    @DisplayName("Should return product when id exists")
    void shouldReturnProductWhenIdExists() {
        Product product = repository.getProductById(1L);
        assertNotNull(product, "Product should not be null for existing id");
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("Should return null when id does not exist")
    void shouldReturnNullWhenIdDoesNotExist() {
        Product product = repository.getProductById(9999L);
        assertNull(product, "Product should be null for non-existing id");
    }

    @Test
    @DisplayName("Should return non-empty list of products")
    void shouldReturnNonEmptyListOfProducts() {
        List<Product> products = repository.getAllProducts();
        assertNotNull(products, "Products list should not be null");
        assertFalse(products.isEmpty(), "Products list should not be empty");
    }
}