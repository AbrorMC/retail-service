package uz.uzumtech.retail_service.dto.response;

import java.math.BigDecimal;

public record FinancialResponse(
    BigDecimal totalRevenue,
    BigDecimal totalCost,
    BigDecimal netProfit
) {
}
