package com.example.meli.comparator.repository;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.utils.ProductLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductRepository {

    // Aqui eu mantenho um mapa estático de produtos para acesso rápido por ID,
    // além da lista estática completa de produtos carregada na inicialização da classe.
    private static final Map<Long, Product> productMap;
    private static final List<Product> products;

    static {
        // Carrego a lista de produtos uma única vez na inicialização da classe,
        // usando o ProductLoader que lê do arquivo JSON.
        products = ProductLoader.loadProducts();

        // Crio um mapa para permitir buscas rápidas pelo ID do produto,
        // o que evita ter que percorrer a lista toda.
        productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    /**
     * Retorna o produto correspondente ao ID informado, se existir.
     * Uso o mapa estático para um acesso eficiente.
     * Registo debug para acompanhar as buscas.
     */
    public Product getProductById(Long id) {
        log.debug("Getting product from Product list by  {}", id);
        return productMap.get(id);
    }

    /**
     * Retorna a lista completa de produtos carregados.
     * A lista é estática e imutável durante o tempo de execução da aplicação.
     * Registo debug para monitorar acessos à lista.
     */
    public List<Product> getAllProducts() {
        log.debug("Getting all products from Product list");
        return products;
    }
}
