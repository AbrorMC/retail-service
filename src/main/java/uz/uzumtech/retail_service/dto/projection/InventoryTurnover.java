package uz.uzumtech.retail_service.dto.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uz.uzumtech.retail_service.constant.enums.MeasurementUnit;

import java.math.BigDecimal;

@JsonPropertyOrder({ "ingredientName", "unit", "incomeQuantity", "outcomeQuantity" })
public interface InventoryTurnover {
    String getIngredientName();
    MeasurementUnit getUnit();
    BigDecimal getIncomeQuantity();
    BigDecimal getOutcomeQuantity();
}
