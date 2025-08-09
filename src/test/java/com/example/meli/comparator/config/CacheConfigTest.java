package com.example.meli.comparator.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class CacheConfigTest {

    @Test
    @DisplayName("CacheManager bean is created and configured correctly")
    void cacheManagerBeanCreatedAndConfigured() {
        try (var context = new AnnotationConfigApplicationContext(CacheConfig.class)) {
            CacheManager cacheManager = context.getBean(CacheManager.class);
            assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);

            CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;
            CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheManager.getCache("filteredProducts");
            assertThat(caffeineCache).isNotNull();

            Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
            assertThat(nativeCache).isNotNull();

            // Não existe met0do direto para pegar expireAfterWrite, mas podemos testar o cache em si
            // Aqui é só um exemplo que o cache foi criado com configurações do Caffeine
            assertThat(nativeCache.policy().expireAfterWrite()).isPresent();

            // Verifica se o maximumSize está configurado (aproximadamente)
            assertThat(nativeCache.policy().eviction()).isPresent();
            assertThat(nativeCache.policy().eviction().get().getMaximum()).isEqualTo(100);
        }
    }
}
