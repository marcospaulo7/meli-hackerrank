package com.example.meli.comparator.service;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import com.example.meli.comparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceimpl service;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productList = List.of(
                Product.builder()
                        .id(1L)
                        .name("Phone")
                        .classification("Electronics")
                        .price(new BigDecimal("777.99"))
                        .description("Smartphone")
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("TV")
                        .classification("Electronics")
                        .price(new BigDecimal("999.99"))
                        .description("Smart TV")
                        .build()
        );
    }

    @Test
    @DisplayName("getProducts returns all products when no filters")
    void getProductsReturnsAllProductsWhenNoFilters() {
        when(repository.getAllProducts()).thenReturn(productList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> result = service.getProducts(Map.of(), pageable);

        assertNotNull(result);
        assertEquals(productList.size(), result.getTotalElements());
        verify(repository, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("getProducts returns filtered products when filters applied")
    void getProductsReturnsFilteredProductsWhenFiltersApplied() {
        when(repository.getAllProducts()).thenReturn(productList);

        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = Map.of("name", "Phone");
        Page<Product> result = service.getProducts(filters, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Phone", result.getContent().get(0).getName());
        verify(repository, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("getProductById returns product when found")
    void getProductByIdReturnsProductWhenFound() {
        when(repository.getProductById(1L)).thenReturn(productList.get(0));

        Product product = service.getProductbyId(1L);
        assertNotNull(product);
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("getProductById throws exception when product not found")
    void getProductByIdThrowsExceptionWhenNotFound() {
        when(repository.getProductById(99L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> service.getProductbyId(99L));
    }
}
