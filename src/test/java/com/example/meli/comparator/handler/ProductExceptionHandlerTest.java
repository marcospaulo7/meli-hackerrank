package com.example.meli.comparator.handler;

import com.example.meli.comparator.handler.exceptions.ConstraintViolationException;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductExceptionHandlerTest {

    private ProductExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new ProductExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/uri");
    }

    @Test
    @DisplayName("should handle ProductNotFoundException and return 404")
    void shouldHandleProductNotFoundException() {
        var ex = new ProductNotFoundException(42L);

        ResponseEntity<ApiError> response = handler.handleProductNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.getPath()).isEqualTo("/test/uri");
        assertThat(body.getMessage()).contains("42");
    }

    @Test
    @DisplayName("should handle generic Exception and return 500")
    void shouldHandleGenericException() {
        var ex = new RuntimeException("Unexpected error");

        ResponseEntity<ApiError> response = handler.handleGenericException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.getMessage()).isEqualTo("Internal server error");
        assertThat(body.getPath()).isEqualTo("/test/uri");
    }

    @Test
    @DisplayName("should handle ConstraintViolationException and return 400")
    void shouldHandleConstraintViolationException() {
        var ex = new ConstraintViolationException("Invalid input");

        ResponseEntity<ApiError> response = handler.handleValidationExceptions(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getMessage()).isEqualTo("Invalid input");
        assertThat(body.getPath()).isEqualTo("/test/uri");
    }

    @Test
    @DisplayName("should handle MethodArgumentTypeMismatchException and return 400")
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Criando um MethodArgumentTypeMismatchException real
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "badValue",          // valor inválido
                String.class,        // tipo esperado
                "paramName",         // nome do parâmetro
                null,                // causa (null)
                null                 // parâmetro do método (null)
        );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).contains("paramName");
        assertThat(body.getMessage()).contains("badValue");
        assertThat(body.getMessage()).contains("String");
        assertThat(body.getPath()).isEqualTo("/test/uri");
    }

    @Test
    @DisplayName("should handle BadRequestException and return 400")
    void shouldHandleBadRequestException() {
        BadRequestException ex = new BadRequestException("Bad request error");

        ResponseEntity<ApiError> response = handler.handleBadRequestException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ApiError body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Bad request error");
        assertThat(body.getPath()).isEqualTo("/test/uri");
    }
}
