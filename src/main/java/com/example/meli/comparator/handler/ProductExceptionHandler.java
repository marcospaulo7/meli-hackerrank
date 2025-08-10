package com.example.meli.comparator.handler;

import com.example.meli.comparator.handler.exceptions.ConstraintViolationException;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class ProductExceptionHandler {

    /**
     * Aqui eu trato especificamente a exceção ProductNotFoundException,
     * que acontece quando um produto não é encontrado no sistema.
     * Eu registro um log de warning para ter rastreabilidade desse caso.
     * Retorno um ResponseEntity com status 404 e um payload ApiError com detalhes do erro.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request) {
        log.warn("Product not found: {} - Request URI: {}", ex.getMessage(), request.getRequestURI());

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Esse mét0do é um catch-all para exceções não tratadas de forma específica.
     * Eu registro um log de erro completo para facilitar o diagnóstico de problemas internos.
     * Retorno um status 500 com mensagem genérica para não expor detalhes internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal server error: {} - Request URI: {}", ex.getMessage(), request.getRequestURI(), ex);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Internal server error",
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Aqui eu trato a exceção customizada ConstraintViolationException,
     * que indica falhas em validação de dados.
     * Faço log de warning e retorno status 400 com detalhes da falha.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("Validation failed: {} - Request URI: {}", ex.getMessage(), request.getRequestURI());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trato exceções causadas por parâmetros de rota ou query que não puderam ser convertidos
     * para o tipo esperado, como passar "abc" onde era esperado um número.
     * Eu uso as informações do ex para montar uma mensagem clara para o cliente.
     * Logo como warning e retorno status 400.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        assert ex.getRequiredType() != null;
        String message = String.format("Parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        log.warn("Type mismatch: {} - Request URI: {}", message, request.getRequestURI());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trato exceções do tipo BadRequestException, que podem ocorrer em várias situações
     * onde a requisição está mal formada.
     * Registro o warning e retorno status 400 com a mensagem da exceção.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.warn("Bad request: {} - Request URI: {}", ex.getMessage(), request.getRequestURI());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
