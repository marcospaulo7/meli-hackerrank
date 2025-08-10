package com.example.meli.comparator.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Aqui eu crio o bean do CacheManager para a aplicação.
     * Optei por usar o Caffeine porque é uma implementação leve e performática para cache em memória.
     * O cache será usado para armazenar resultados filtrados dos produtos,
     * facilitando respostas mais rápidas em chamadas repetidas com os mesmos filtros.
     */
    @Bean
    public CacheManager cacheManager() {
        // Crio um cache manager específico para a cache chamada "filteredProducts"
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("filteredProducts");

        // Configuro o cache para expirar 5 segundos após a escrita,
        // o que significa que a informação fica "fresca" por um curto período para equilibrar desempenho e atualidade dos dados.
        // Também limitei o tamanho máximo para 100 entradas para evitar uso excessivo de memória.
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(5))
                        .maximumSize(100)
        );
        return cacheManager;
    }
}


