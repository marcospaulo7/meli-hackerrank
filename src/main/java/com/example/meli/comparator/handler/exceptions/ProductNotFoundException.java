package com.example.meli.comparator.handler.exceptions;

/**
 * Exceção personalizada para indicar que um produto com determinado ID não foi encontrado.
 *
 * Uso: lanço essa exceção quando a busca por um produto pelo seu ID não retorna resultado,
 * sinalizando que o recurso solicitado não existe no sistema.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Construtor que recebe o ID do produto não encontrado para construir a mensagem de erro.
     *
     * @param id Identificador do produto que não foi localizado
     */
    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
