package com.example.meli.comparator.repository;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.utils.ProductLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Getting product from Product list by  {}", id);
        return productMap.get(id);
    }

    public List<Product> getAllProducts() {
        log.debug("Getting all products from Product list");
        return products;
    }
}
