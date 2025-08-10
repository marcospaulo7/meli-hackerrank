package com.example.meli.comparator.handler;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Classe que encapsula os detalhes do erro retornado pela API.
 * Eu defini os campos como finais para garantir imutabilidade após a criação do objeto.
 */
@Getter
public class ApiError {
    /**
     * Código HTTP do status da resposta (ex: 400, 404, 500).
     */
    private final int status;

    /**
     * Momento exato em que o erro foi criado, para auxiliar no rastreamento.
     */
    private final LocalDateTime timestamp;

    /**
     * Texto padrão que representa o status do erro (ex: "Not Found", "Bad Request").
     */
    private final String error;

    /**
     * Mensagem detalhada que explica o motivo do erro, geralmente mais específica.
     */
    private final String message;

    /**
     * Caminho da requisição que gerou o erro, para contexto do cliente e logs.
     */
    private final String path;

    /**
     * Construtor que inicializa os dados do erro.
     * Ao criar a instância, eu já seto o timestamp para o instante atual.
     *
     * @param status código HTTP do erro
     * @param error texto descritivo do status
     * @param message mensagem explicativa
     * @param path URI da requisição que causou o erro
     */
    public ApiError(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
