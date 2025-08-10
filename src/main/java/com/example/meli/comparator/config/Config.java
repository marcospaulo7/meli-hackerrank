package com.example.meli.comparator.config;

import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.service.impl.ProductServiceimpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Config {

    /**
     * Aqui eu crio o bean do ProductService para ser injetado onde for necessário.
     * Usei @Primary para garantir que, caso haja múltiplas implementações,
     * essa seja a principal escolhida pelo Spring.
     * Isso facilita testes e configurações, além de deixar explícito qual implementação será usada.
     */
    @Bean
    @Primary
    public ProductService getProductService() {
        // Retorno a implementação concreta do serviço
        return new ProductServiceimpl();
    }
}
