package com.example.meli.comparator.config;

import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.service.ProductServiceimpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Config {

    @Bean
    @Primary
    public ProductService getProductService(){
        return new ProductServiceimpl();
    }
}
