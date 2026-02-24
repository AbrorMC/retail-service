package uz.uzumtech.retail_service.dto.projection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uz.uzumtech.retail_service.constant.enums.MeasurementUnit;

import java.math.BigDecimal;

@JsonPropertyOrder({ "ingredientName", "unit", "actualStock", "totalCost" })
public interface InventoryStock {
    String getIngredientName();
    MeasurementUnit getUnit();
    BigDecimal getActualStock();
    BigDecimal getTotalCost();
}
