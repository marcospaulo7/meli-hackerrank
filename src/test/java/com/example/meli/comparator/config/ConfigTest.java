package com.example.meli.comparator.config;

import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.service.ProductServiceimpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void getProductServiceShouldReturnInstance() {
        ProductService service = new ProductServiceimpl();
        assertNotNull(service);
        assertInstanceOf(ProductService.class, service);
    }
}
