package com.example.meli.comparator.service.impl;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.exceptions.ProductNotFoundException;
import com.example.meli.comparator.repository.ProductRepository;
import com.example.meli.comparator.service.ProductService;
import com.example.meli.comparator.utils.ProductFilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ProductServiceimpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    // Defino quais filtros são permitidos para evitar processamento desnecessário ou inseguro.
    private static final Set<String> ALLOWED_FILTERS = Set.of(
            "name", "classification", "price", "description"
    );

    /**
     * Mét0do principal para obter produtos, com suporte a paginação e filtros opcionais.
     * Uso cache para melhorar a performance em chamadas repetidas com os mesmos parâmetros.
     *
     * @param filters  mapa de filtros opcionais (ex: nome, classificação)
     * @param pageable paginação (página atual, tamanho da página)
     * @return página de produtos filtrados e paginados
     */
    @Override
    @Cacheable(value = "filteredProducts", key = "#filters.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Product> getProducts(Map<String, String> filters, Pageable pageable) {
        // Se filtros foram informados, aplico os filtros à lista completa
        List<Product> filteredList = (filters != null && !filters.isEmpty())
                ? applyFilters(repository.getAllProducts(), filters)
                : repository.getAllProducts();

        // Retorno a página solicitada da lista filtrada
        return getPage(filteredList, pageable);
    }

    /**
     * Aplica filtros permitidos à lista de produtos.
     * Uso stream para aplicar cada filtro sequencialmente.
     * Aproveito a utilidade ProductFilterUtils para a lógica de comparação.
     *
     * @param productsList lista completa de produtos
     * @param filters      filtros para aplicar (chave, valor)
     * @return lista filtrada
     */
    private List<Product> applyFilters(List<Product> productsList, Map<String, String> filters) {
        Stream<Product> stream = productsList.stream();

        log.info("Filtering product list");
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            String filterValue = entry.getValue();

            if (ALLOWED_FILTERS.contains(fieldName)) {
                log.info("Filter {} used allowed", fieldName);
                // Aplico o filtro para cada produto usando a utilidade especializada
                stream = stream.filter(product -> ProductFilterUtils.matchesFilter(product, fieldName, filterValue));
            }
            // Caso o filtro não seja permitido, ignoro silenciosamente (sem lançar erro)
        }

        // Coleto o resultado filtrado numa lista para processamento posterior
        return stream.collect(Collectors.toList());
    }

    /**
     * Gero a página solicitada a partir da lista filtrada.
     * Uso o offset e tamanho da página do objeto Pageable.
     *
     * @param filteredList lista já filtrada
     * @param pageable     objeto com dados de paginação
     * @return página de produtos contendo sublista, info de paginação e total de elementos
     */
    private Page<Product> getPage(List<Product> filteredList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredList.size());

        // Aqui precisa de um tratamento para caso start seja maior que filteredList.size()
        if (start > filteredList.size()) {
            // Retornar página vazia para evitar exceção
            return new PageImpl<>(List.of(), pageable, filteredList.size());
        }

        // Sublista para a página específica
        List<Product> paged = filteredList.subList(start, end);

        return new PageImpl<>(paged, pageable, filteredList.size());
    }

    /**
     * Busca um produto pelo seu ID.
     * Caso não encontre, lanço exceção específica para o domínio.
     *
     * @param id ID do produto
     * @return produto encontrado
     * @throws ProductNotFoundException se produto não existir
     */
    @Override
    public Product getProductbyId(Long id) {
        return Optional.ofNullable(repository.getProductById(id))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
