package uz.uzumtech.retail_service.repository;

import java.math.BigDecimal;

public interface IngredientRequirement {
    Long getIngredientId();
    BigDecimal getTotalQuantity();
}
