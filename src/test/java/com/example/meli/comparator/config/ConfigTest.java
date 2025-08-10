package com.example.meli.comparator.config;

import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.service.impl.ProductServiceimpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigTest {

    @Test
    void getProductServiceShouldReturnInstance() {
        ProductService service = new ProductServiceimpl();
        assertNotNull(service);
        assertInstanceOf(ProductService.class, service);
    }
}
