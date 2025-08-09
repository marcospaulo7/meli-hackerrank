package com.example.meli.comparator.repository;

import com.example.meli.comparator.data.Product;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProductLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows

    public static List<Product> loadProducts() {
        try (InputStream inputStream = ProductLoader.class.getResourceAsStream("/products.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("products.json file not found in resources");
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {
            });
        } catch (JsonParseException | JsonMappingException e) {
            throw new RuntimeException("Invalid JSON format in products.json: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading products.json: " + e.getMessage(), e);
        }
    }
}
