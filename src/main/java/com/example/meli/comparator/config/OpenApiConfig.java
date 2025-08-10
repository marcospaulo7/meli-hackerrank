package com.example.meli.comparator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Eu configuro o bean OpenAPI para personalizar a documentação da API.
     * Isso permite que a especificação OpenAPI gerada tenha um título,
     * versão e descrição claros, o que melhora o entendimento para quem consumir a API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product search API") // Título da API
                        .version("1.0.0")            // Versão atual da API
                        .description("API to provide product details for comparison")); // Descrição resumida do propósito da API
    }
}
