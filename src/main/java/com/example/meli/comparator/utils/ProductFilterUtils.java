package com.example.meli.comparator.utils;

import com.example.meli.comparator.data.Product;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;

@Slf4j
public class ProductFilterUtils {

    // Eu deixo o construtor privado porque essa classe é utilitária e não deve ser instanciada
    private ProductFilterUtils() {
    }

    /**
     * Mét0do principal para verificar se o produto corresponde ao filtro passado.
     *
     * Eu uso reflexão para acessar o campo do produto dinamicamente pelo nome,
     * pois quero aplicar filtros genéricos em vários campos sem precisar de muitos ifs.
     *
     * Depois que pego o valor do campo, trato especialmente valores do tipo BigDecimal,
     * já que a comparação numérica precisa ser diferente (não dá pra usar equals direto).
     *
     * Para os demais tipos, eu comparo a String ignorando caixa alta/baixa para maior flexibilidade.
     *
     * Se o campo não existir ou não puder ser acessado, eu aviso no log e retorno false,
     * porque isso significa que o filtro não se aplica.
     *
     * @param product o produto que eu quero filtrar
     * @param fieldName o nome do campo que eu quero usar como filtro
     * @param filterValue o valor do filtro para comparar
     * @return true se o produto corresponde ao filtro, false caso contrário
     */
    public static boolean matchesFilter(Product product, String fieldName, String filterValue) {
        try {
            Field field = Product.class.getDeclaredField(fieldName);
            field.setAccessible(true); // quebro encapsulamento para acessar o campo privado

            Object fieldValue = field.get(product);
            if (fieldValue == null) {
                // Se o campo for null, não faz sentido continuar comparando
                log.debug("Field '{}' is null for product id '{}', filterValue='{}' - no match", fieldName, product.getId(), filterValue);
                return false;
            }

            if (fieldValue instanceof BigDecimal) {
                // Se for número, uso mét0do específico para comparação numérica precisa
                boolean result = compareBigDecimal(filterValue, (BigDecimal) fieldValue);
                log.debug("Comparing BigDecimal field '{}' for product id '{}': filterValue='{}', fieldValue='{}', match={}",
                        fieldName, product.getId(), filterValue, fieldValue, result);
                return result;
            }

            // Para outros tipos, comparo a string ignorando maiúsculas/minúsculas para evitar falhas bobas
            boolean isFieldValueEqual = fieldValue.toString().equalsIgnoreCase(filterValue);
            log.debug("Comparing field '{}' for product id '{}': filterValue='{}', fieldValue='{}', match={}",
                    fieldName, product.getId(), filterValue, fieldValue, isFieldValueEqual);
            return isFieldValueEqual;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Se o campo não existe ou não pode ser acessado, eu aviso e ignoro o filtro
            log.warn("Illegal access to field '{}' on Product class for product id '{}'. Skipping filter.", fieldName, product.getId());
            return false;
        }
    }

    /**
     * Mét0do auxiliar para comparar um valor string que deve representar um número decimal
     * com o valor do campo BigDecimal do produto.
     *
     * Tento converter o valor para BigDecimal e comparo com o valor do campo usando compareTo.
     * Se o valor do filtro não for um número válido, eu aviso no log e retorno false para ignorar o filtro.
     *
     * @param value valor do filtro (string)
     * @param fieldValue valor do campo do produto (BigDecimal)
     * @return true se os valores forem iguais numericamente, false caso contrário ou em caso de erro
     */
    public static boolean compareBigDecimal(String value, BigDecimal fieldValue) {
        try {
            BigDecimal filterDecimal = new BigDecimal(value);
            return filterDecimal.compareTo(fieldValue) == 0;
        } catch (NumberFormatException e) {
            // Valor inválido no filtro, ignoro para não travar a aplicação
            log.warn("Invalid BigDecimal filter value '{}'. Skipping this filter.", value);
            return false;
        }
    }

}

