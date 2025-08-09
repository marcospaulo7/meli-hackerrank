package com.example.meli.comparator.controller;


import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam Map<String, String> filters,
            Pageable pageable) {

        Page<Product> page = service.getProducts(filters, pageable);


        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Product> getProductById(
            @PathVariable("id")
            Long id) {

        var product = service.getProductbyId(id);

        return ResponseEntity.ok(product);
    }
}
