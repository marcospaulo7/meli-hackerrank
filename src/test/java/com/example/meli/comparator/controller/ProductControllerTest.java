package com.example.meli.comparator.controller;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import com.example.meli.comparator.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.TestConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    private Product sampleProduct;
    private Page<Product> samplePage;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @BeforeEach
    void setup() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Sample Product")
                .classification("Electronics")
                .price(BigDecimal.valueOf(99.99))
                .build();

        samplePage = new PageImpl<>(Collections.singletonList(sampleProduct), PageRequest.of(0, 10), 1);
    }

    @Test
    @DisplayName("should return paginated products with filters")
    void shouldReturnPaginatedProductsWithFilters() throws Exception {
        Mockito.when(productService.getProducts(any(Map.class), any(Pageable.class))).thenReturn(samplePage);

        mockMvc.perform(get("/products")
                        .param("classification", "Electronics")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(sampleProduct.getId()))
                .andExpect(jsonPath("$.content[0].name").value(sampleProduct.getName()))
                .andExpect(jsonPath("$.content[0].classification").value(sampleProduct.getClassification()))
                .andExpect(jsonPath("$.content[0].price").value(sampleProduct.getPrice().doubleValue()));
    }

    @Test
    @DisplayName("should return product by id")
    void shouldReturnProductById() throws Exception {
        Mockito.when(productService.getProductbyId(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
                .andExpect(jsonPath("$.name").value(sampleProduct.getName()))
                .andExpect(jsonPath("$.classification").value(sampleProduct.getClassification()))
                .andExpect(jsonPath("$.price").value(sampleProduct.getPrice().doubleValue()));
    }

    @Test
    @DisplayName("should return 404 when product not found by id")
    void shouldReturn404WhenProductNotFoundById() throws Exception {
        Mockito.when(productService.getProductbyId(999L))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/products/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
