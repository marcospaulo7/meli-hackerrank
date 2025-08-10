package com.example.meli.comparator.service;

import com.example.meli.comparator.config.CacheConfig;
import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import com.example.meli.comparator.repository.ProductRepository;
import com.example.meli.comparator.service.impl.ProductServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
@Import(CacheConfig.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceimpl service;

    private List<Product> productList;

    @BeforeEach
    void setUp() {

        productList = List.of(
                Product.builder()
                        .id(1L)
                        .name("Phone")
                        .classification("Electronics")
                        .price(new BigDecimal("777.99"))
                        .description("Smartphone")
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("TV")
                        .classification("Electronics")
                        .price(new BigDecimal("999.99"))
                        .description("Smart TV")
                        .build()
        );
    }

    @Test
    @DisplayName("getProducts returns all products when no filters")
    void shouldReturnAllProductsWhenNoFilters() {
        when(repository.getAllProducts()).thenReturn(productList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> result = service.getProducts(Map.of(), pageable);

        assertNotNull(result);
        assertEquals(productList.size(), result.getTotalElements());
        verify(repository, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("getProducts returns all products when filters is null")
    void shouldReturnAllProductsWhenFiltersIsNull() {
        when(repository.getAllProducts()).thenReturn(productList);

        Page<Product> result = service.getProducts(null, PageRequest.of(0, 10));

        assertThat(result.getContent()).containsExactlyElementsOf(productList);
        verify(repository, times(1)).getAllProducts();
    }


    @Test
    @DisplayName("getProducts returns filtered products when filters applied")
    void shouldReturnFilteredProductsWhenFiltersApplied() {
        when(repository.getAllProducts()).thenReturn(productList);

        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = Map.of("name", "Phone");
        Page<Product> result = service.getProducts(filters, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Phone", result.getContent().get(0).getName());
        verify(repository, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("getProducts returns all products when filters are not allowed")
    void shouldReturnAllProductsWhenFiltersAreNotAllowed() {

        when(repository.getAllProducts()).thenReturn(productList);

        Map<String, String> filters = Map.of("cor", "vermelho");

        Page<Product> result = service.getProducts(filters, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2); // lista completa
        assertThat(result.getContent()).containsExactlyElementsOf(productList);

        verify(repository, times(1)).getAllProducts();

        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("getProductById returns product when found")
    void shouldReturnProductByIdWhenFound() {
        when(repository.getProductById(1L)).thenReturn(productList.get(0));

        Product product = service.getProductbyId(1L);
        assertNotNull(product);
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("getProductById throws exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.getProductById(99L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> service.getProductbyId(99L));
    }

    @Test
    @DisplayName("Should cache filtered products and avoid repeated repository calls")
    void shouldCacheFilteredProducts() {
        // Configuro o mock para retornar a lista de produtos ao chamar getAllProducts()
        when(repository.getAllProducts()).thenReturn(productList);

        Map<String, String> filters = Map.of("name", "Phone");
        Pageable pageable = PageRequest.of(0, 10);

        // Na primeira chamada com esses filtros, o serviço deve buscar no repositório
        Page<Product> firstCall = service.getProducts(filters, pageable);
        assertEquals("Phone", firstCall.getContent().getFirst().getName());
        verify(repository, times(1)).getAllProducts();

        // Na segunda chamada com os mesmos parâmetros, o resultado deve vir do cache,
        // ou seja, o repositório não deve ser chamado novamente
        Page<Product> secondCall = service.getProducts(filters, pageable);
        assertEquals("Phone", secondCall.getContent().getFirst().getName());

        // Verifico que não houve nenhuma outra interação com o repositório após a primeira chamada
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should return empty page when requested page start index is out of bounds")
    void shouldReturnEmptyPageWhenPageIsOutOfBounds() {
        when(repository.getAllProducts()).thenReturn(productList);

        Pageable pageable = PageRequest.of(10, 10); // start = 10 * 10 = 100 > 2

        Page<Product> result = service.getProducts(null, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected empty page when start index exceeds list size");
        assertEquals(0, result.getContent().size());
        assertEquals(productList.size(), result.getTotalElements());
    }

}
