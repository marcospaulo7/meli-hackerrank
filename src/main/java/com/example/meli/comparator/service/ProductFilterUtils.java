package com.example.meli.comparator.service;

import com.example.meli.comparator.data.Product;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class ProductFilterUtils {

    private ProductFilterUtils() {
    }

    public static boolean matchesFilter(Product product, String fieldName, String filterValue) {
        try {
            Field field = Product.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            Object fieldValue = field.get(product);
            if (fieldValue == null) {
                return false;
            }

            if (fieldValue instanceof BigDecimal) {
                return compareBigDecimal(filterValue, (BigDecimal) fieldValue);
            }

            return fieldValue.toString().equalsIgnoreCase(filterValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    public static boolean compareBigDecimal(String value, BigDecimal fieldValue) {
        try {
            BigDecimal filterDecimal = new BigDecimal(value);
            return filterDecimal.compareTo(fieldValue) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
