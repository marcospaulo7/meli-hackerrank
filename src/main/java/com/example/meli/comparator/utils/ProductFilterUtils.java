package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;

@Slf4j
public class ProductFilterUtils {

    private ProductFilterUtils() {
    }

    public static boolean matchesFilter(Product product, String fieldName, String filterValue) {
        try {
            Field field = Product.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            Object fieldValue = field.get(product);
            if (fieldValue == null) {
                log.debug("Field '{}' is null for product id '{}', filterValue='{}' - no match", fieldName, product.getId(), filterValue);
                return false;
            }

            if (fieldValue instanceof BigDecimal) {
                boolean result = compareBigDecimal(filterValue, (BigDecimal) fieldValue);
                log.debug("Comparing BigDecimal field '{}' for product id '{}': filterValue='{}', fieldValue='{}', match={}",
                        fieldName, product.getId(), filterValue, fieldValue, result);
                return result;
            }

            boolean isFieldValueEqual = fieldValue.toString().equalsIgnoreCase(filterValue);
            log.debug("Comparing field '{}' for product id '{}': filterValue='{}', fieldValue='{}', match={}",
                    fieldName, product.getId(), filterValue, fieldValue, isFieldValueEqual);
            return isFieldValueEqual;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Illegal access to field '{}' on Product class for product id '{}'. Skipping filter.", fieldName, product.getId());
            return false;
        }
    }

    public static boolean compareBigDecimal(String value, BigDecimal fieldValue) {
        try {
            BigDecimal filterDecimal = new BigDecimal(value);
            return filterDecimal.compareTo(fieldValue) == 0;
        } catch (NumberFormatException e) {
            log.warn("Invalid BigDecimal filter value '{}'. Skipping this filter.", value);
            return false;
        }
    }

}
