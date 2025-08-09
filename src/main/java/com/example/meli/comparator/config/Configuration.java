package com.example.meli.comparator.config;

import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.service.ProductServiceimpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @Primary

    public ProductService getProductService(){
        return new ProductServiceimpl();
    }


}
