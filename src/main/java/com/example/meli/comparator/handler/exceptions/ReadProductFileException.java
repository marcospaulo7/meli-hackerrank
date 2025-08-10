package com.example.meli.comparator.handler.exceptions;

import java.io.IOException;

/**
 * Exceção personalizada para erros ocorridos durante a leitura do arquivo de produtos.
 *
 * Uso: lanço essa exceção quando há problemas na leitura do arquivo JSON que contém os dados dos produtos,
 * seja por erro de I/O ou formato inválido do arquivo.
 */
public class ReadProductFileException extends RuntimeException {

    /**
     * Construtor que recebe uma mensagem descritiva e a exceção original de I/O que causou o problema.
     *
     * @param message Mensagem descritiva do erro ocorrido durante a leitura do arquivo
     * @param e Exceção original do tipo IOException que foi capturada
     */
    public ReadProductFileException(String message, IOException e) {
        super(message, e);
    }
}
