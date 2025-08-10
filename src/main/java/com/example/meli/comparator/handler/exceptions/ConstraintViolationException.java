package com.example.meli.comparator.handler.exceptions;

/**
 * Exceção personalizada para representar violações de restrições de validação.
 *
 * Uso: lanço essa exceção quando alguma regra de validação do domínio não é cumprida,
 * por exemplo, dados inválidos ou formatados incorretamente que impedem a continuidade da operação.
 */
public class ConstraintViolationException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem da violação para detalhar o erro.
     *
     * @param message Mensagem explicando a violação de restrição
     */
    public ConstraintViolationException(String message) {
        super(message);
    }
}
