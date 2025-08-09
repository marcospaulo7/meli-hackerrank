package com.example.meli.comparator.repository;


import com.example.meli.comparator.data.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();
    }

    @Test
    @DisplayName("getProductById returns product when id exists")
    void getProductByIdReturnsProductWhenIdExists() {
        Product product = repository.getProductById(1L);
        assertNotNull(product, "Product should not be null for existing id");
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("getProductById returns null when id does not exist")
    void getProductByIdReturnsNullWhenIdDoesNotExist() {
        Product product = repository.getProductById(9999L);
        assertNull(product, "Product should be null for non-existing id");
    }

    @Test
    @DisplayName("getAllProducts returns non-empty list")
    void getAllProductsReturnsNonEmptyList() {
        List<Product> products = repository.getAllProducts();
        assertNotNull(products, "Products list should not be null");
        assertFalse(products.isEmpty(), "Products list should not be empty");
    }

}