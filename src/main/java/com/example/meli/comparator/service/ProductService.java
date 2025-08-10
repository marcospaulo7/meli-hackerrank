package com.example.meli.comparator.service;

import com.example.meli.comparator.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Interface que define os serviços relacionados a produtos.
 * Aqui declaro os métodos para buscar produto por ID e para
 * obter uma lista paginada e filtrada de produtos.
 */
public interface ProductService {

    /**
     * Mét0do para buscar um produto específico pelo seu ID.
     * Espero que a implementação lance uma exceção caso o produto
     * não seja encontrado para facilitar o tratamento de erros.
     *
     * @param id identificador único do produto
     * @return produto correspondente ao ID
     */
    Product getProductbyId(Long id);

    /**
     * Mét0do para obter uma página de produtos, com possibilidade
     * de aplicar filtros flexíveis via mapa de chave-valor.
     * Também recebe parâmetros de paginação para limitar resultados.
     *
     * @param filters mapa de filtros opcionais (ex: "name" -> "phone")
     * @param pageable objeto com informações de página e tamanho
     * @return página de produtos resultante da filtragem e paginação
     */
    Page<Product> getProducts(Map<String, String> filters, Pageable pageable);
}
