package com.example.meli.comparator.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private String classification;
    private Map<String, String> specifications;

}
