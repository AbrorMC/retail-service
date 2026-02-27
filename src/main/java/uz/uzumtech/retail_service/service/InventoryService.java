package uz.uzumtech.retail_service.service;

import java.math.BigDecimal;

public interface InventoryService {

    BigDecimal consumeIngredients(Long orderId);
    BigDecimal getIncome(Long orderId);

}
