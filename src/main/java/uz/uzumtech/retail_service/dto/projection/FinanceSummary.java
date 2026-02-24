package uz.uzumtech.retail_service.dto.projection;

import java.math.BigDecimal;

public interface FinanceSummary {
    BigDecimal getTotalIncome();
    BigDecimal getTotalExpense();
}
