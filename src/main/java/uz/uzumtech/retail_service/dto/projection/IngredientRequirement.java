package uz.uzumtech.retail_service.dto.projection;

import java.math.BigDecimal;

public interface IngredientRequirement {
    Long getIngredientId();
    BigDecimal getTotalQuantity();
}
