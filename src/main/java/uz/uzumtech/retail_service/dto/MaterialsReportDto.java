package uz.uzumtech.retail_service.dto;

import uz.uzumtech.retail_service.constant.enums.MeasurementUnit;

import java.math.BigDecimal;

public record MaterialsReportDto (
    String ingredientName,
    MeasurementUnit unit,
    BigDecimal stockBegin,
    BigDecimal costBegin,
    BigDecimal incomeQuantity,
    BigDecimal outcomeQuantity,
    BigDecimal stockEnd,
    BigDecimal costEnd
)
{}
