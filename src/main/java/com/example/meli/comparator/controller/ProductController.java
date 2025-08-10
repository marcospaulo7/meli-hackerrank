package com.example.meli.comparator.controller;


import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    /* Eu injetei o serviço para lidar com a lógica de negócio relacionada a produtos.
     Quero manter o controller focado em receber requisições e devolver respostas, delegando a lógica para o service. */
    @Autowired
    private ProductService service;

    @GetMapping
    @Operation(
            summary = "Get paginated list of products with optional filters",
            description = "Returns a paginated list of products. You can optionally filter by any allowed product attribute, " +
                    "such as classification, name, or price via query parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of products retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Filters to apply as key-value pairs", example = "{\"classification\":\"Electronics\", \"color\":\"red\"}")
            @RequestParam Map<String, String> filters,

            @Parameter(hidden = true)
            Pageable pageable) {

        // Aqui eu registro no log os filtros recebidos para ajudar na análise de chamadas e debugging.
        log.info("Getting products by filters {} ", filters);

        // Passo os filtros e a paginação para o serviço, que vai retornar os dados já preparados.
        Page<Product> page = service.getProducts(filters, pageable);

        // Confirma no log que a busca foi realizada com sucesso.
        log.info("Success getting products by filters - STATUS 200");

        // Retorno a resposta HTTP 200 com o conteúdo paginado dos produtos.
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product to be obtained")
            @PathVariable Long id) {

        // Registro o id solicitado para acompanhar requisições específicas.
        log.info("Getting product by id {} ", id);

        // Chamo o serviço para obter o produto pelo id. Caso não exista, o serviço pode lançar exceção.
        var product = service.getProductbyId(id);

        // Registro o sucesso da operação junto com os dados do produto encontrado.
        log.info("Success getting product by id {} - STATUS 200 - Product: {}.", id, product);

        // Retorno o produto com status HTTP 200 OK.
        return ResponseEntity.ok(product);
    }
}
