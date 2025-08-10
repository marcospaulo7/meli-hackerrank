package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ReadProductFileException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ProductLoader {

    // Eu uso um ObjectMapper estático para evitar criar uma nova instância toda vez,
    // garantindo performance e reutilização ao desserializar JSON.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Mét0do principal para carregar a lista de produtos a partir do arquivo JSON na pasta resources.
     *
     * Aqui eu abro um InputStream para o arquivo "products.json" usando o mét0do getProductStream(),
     * que retorna o recurso do classpath. Caso o arquivo não seja encontrado, lanço uma exceção
     * informando o problema.
     *
     * A leitura do JSON é feita para uma lista genérica de objetos Product usando TypeReference.
     *
     * Eu trato exceções específicas de parsing (JSON mal formatado) e IO,
     * encapsulando elas em uma exceção customizada ReadProductFileException para melhor controle
     * de erro na camada superior.
     *
     * Uso @SneakyThrows para não precisar declarar throws, confiando que o tratamento é feito no catch.
     *
     * @return Lista de produtos carregada do arquivo JSON.
     */
    @SneakyThrows
    public static List<Product> loadProducts() {
        log.info("Getting all products from file /products.json ");
        try (InputStream inputStream = getProductStream()) {
            if (inputStream == null) {
                // Aqui eu deixo claro que o arquivo é obrigatório e sua ausência é uma falha crítica.
                throw new IllegalStateException("products.json file not found in resources");
            }
            // Desserializo o JSON para a lista de produtos
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (JsonParseException | JsonMappingException e) {
            // Exceções de formato JSON incorreto são convertidas para exceção customizada
            throw new ReadProductFileException("Invalid JSON format in products.json: " + e.getMessage(), e);
        } catch (IOException e) {
            // Erros de leitura geral também são tratados da mesma forma para unificar tratamento
            throw new ReadProductFileException("Error reading products.json: " + e.getMessage(), e);
        }
    }

    /**
     * Mét0do utilitário para obter o InputStream do arquivo products.json no classpath.
     * Separei esse mét0do para facilitar testes (mock) e para deixar o código mais organizado.
     *
     * @return InputStream do arquivo products.json ou null se não encontrado.
     */
    static InputStream getProductStream() {
        return ProductLoader.class.getResourceAsStream("/products.json");
    }
}
