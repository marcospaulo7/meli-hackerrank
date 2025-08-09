package com.example.meli.comparator.repository;

import com.example.meli.comparator.data.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductRepository {
    static Map<Long, Product> productMap;
    static List<Product> products;

    static {
        products = ProductLoader.loadProducts();

        productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    public Product getProductById(Long id) {
        return productMap.get(id);
    }

    public List<Product> getAllProducts() {
        return products;
    }
}
