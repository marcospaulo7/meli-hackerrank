package com.example.meli.comparator.service;

import com.example.meli.comparator.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductService {

    Product getProductbyId(Long id);

    Page<Product> getProducts(Map<String, String> filters, Pageable pageable);
}
