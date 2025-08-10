package com.example.meli.comparator.helper;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Cache simples em memória (não expira) para testes
        return new ConcurrentMapCacheManager("filteredProducts");
    }
}
