package uz.uzumtech.retail_service.dto.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uz.uzumtech.retail_service.constant.enums.MeasurementUnit;

import java.math.BigDecimal;

@JsonPropertyOrder({ "ingredientName", "unit", "stockBegin", "costBegin",
        "incomeQuantity", "outcomeQuantity", "stockEnd", "costEnd"})
public interface MaterialsReport {
    String getIngredientName();
    MeasurementUnit getUnit();
    BigDecimal getStockBegin();
    BigDecimal getCostBegin();
    BigDecimal getIncomeQuantity();
    BigDecimal getOutcomeQuantity();
    BigDecimal getStockEnd();
    BigDecimal getCostEnd();
}
