package com.example.meli.comparator.service;

import com.example.meli.comparator.data.Product;
import com.example.meli.comparator.handler.ProductNotFoundException;
import com.example.meli.comparator.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.meli.comparator.service.ProductFilterUtils.matchesFilter;

@Service
public class ProductServiceimpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    private static final Set<String> ALLOWED_FILTERS = Set.of(
            "name", "classification", "price", "description"
    );

//    @Override
//    public Page<Product> getAllProducts(Pageable pageable) {
//        List<Product> products = repository.getAllProducts();
//
//        return getPage(products, pageable);
//    }

    @Override
    public Page<Product> getProducts(Map<String, String> filters, Pageable pageable) {

        List<Product> filteredList = (filters != null && !filters.isEmpty())
                ? applyFilters(repository.getAllProducts(), filters)
                : repository.getAllProducts();

        return getPage(filteredList, pageable);
    }

    private List<Product> applyFilters(List<Product> productsList, Map<String, String> filters) {
        Stream<Product> stream = productsList.stream();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            String filterValue = entry.getValue();

            if (ALLOWED_FILTERS.contains(fieldName)) {
                stream = stream.filter(product -> ProductFilterUtils.matchesFilter(product, fieldName, filterValue));
            }
        }

        return stream.collect(Collectors.toList());
    }

    private Page<Product> getPage(List<Product> filteredList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredList.size());
        List<Product> paged = filteredList.subList(start, end);
        return new PageImpl<>(paged, pageable, filteredList.size());
    }

    @Override
    public Product getProductbyId(Long id) {

        return Optional.ofNullable(repository.getProductById(id))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
