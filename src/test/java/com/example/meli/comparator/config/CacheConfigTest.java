package com.example.meli.comparator.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class CacheConfigTest {

    @Test
    @DisplayName("CacheManager bean is created and configured correctly")
    void cacheManagerBeanCreatedAndConfigured() {
        // Crio um contexto Spring com a configuração CacheConfig para testar o bean CacheManager
        try (var context = new AnnotationConfigApplicationContext(CacheConfig.class)) {
            // Busco o bean do tipo CacheManager
            CacheManager cacheManager = context.getBean(CacheManager.class);
            // Verifico se é uma instância do tipo CaffeineCacheManager, que é o que espero
            assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);

            // Faço cast para CaffeineCacheManager para acessar detalhes específicos
            CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;
            // Recupero o cache nomeado "filteredProducts"
            CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheManager.getCache("filteredProducts");
            // Verifico que o cache foi criado e não é nulo
            assertThat(caffeineCache).isNotNull();

            // Pego o cache nativo do Caffeine para acessar configurações internas
            Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
            // Verifico que o cache nativo não é nulo
            assertThat(nativeCache).isNotNull();

            // Não existe mét0do direto para recuperar expireAfterWrite, mas consigo verificar se a política está presente
            // Isso me garante que a configuração foi aplicada no cache
            assertThat(nativeCache.policy().expireAfterWrite()).isPresent();

            // Também verifico se a política de eviction (limite de tamanho) está presente
            assertThat(nativeCache.policy().eviction()).isPresent();
            // Confirma que o tamanho máximo configurado é 100, conforme definido na configuração
            assertThat(nativeCache.policy().eviction().get().getMaximum()).isEqualTo(100);
        }
    }
}
