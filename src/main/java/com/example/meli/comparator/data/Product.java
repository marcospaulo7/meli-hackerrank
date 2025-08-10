package com.example.meli.comparator.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Produto contendo informações básicas para exibição e comparação")
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos no JSON recebido
@JsonInclude(JsonInclude.Include.NON_NULL) // Não inclui campos nulos no JSON de saída
public class Product {


    @Schema(description = "Identificador único do produto")
    private Long id;

    @Schema(description = "Nome do produto para exibição e busca")
    private String name;

    @Schema(description = "URL da imagem do produto (pode ser nulo)")
    private String imageUrl;

    @Schema(description = "Breve descrição do produto")
    private String description;

    @Schema(description = "Preço do produto com precisão monetária")
    private BigDecimal price;

    @Schema(description = "Classificação ou categoria para organização e filtros")
    private String classification;

    @Schema(description = "Especificações técnicas ou características adicionais do produto")
    private Map<String, String> specifications;
}
