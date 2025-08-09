package com.example.meli.comparator.controller;


import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    @Operation(
            summary = "Get paginated list of products with optional filters",
            description = "Returns a paginated list of products. You can optionally filter by any allowed product attribute, " +
                    "such as classification, name, or price via query parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of products retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Filters to apply as key-value pairs", example = "{\"classification\":\"Electronics\", \"color\":\"red\"}")
            @RequestParam Map<String, String> filters,

            @Parameter(hidden = true)
            Pageable pageable) {

        log.info("Getting products by filters {} ", filters);

        Page<Product> page = service.getProducts(filters, pageable);

        log.info("Success getting products by filters - STATUS 200");

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product to be obtained")
            @PathVariable Long id) {

        log.info("Getting product by id {} ", id);

        var product = service.getProductbyId(id);

        log.info("Success getting product by id {} - STATUS 200 - Product: {}.", id, product);

        return ResponseEntity.ok(product);
    }
}
