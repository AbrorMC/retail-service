package uz.uzumtech.retail_service.service;

import java.math.BigDecimal;

public interface ReportService {

    void registerIncome(BigDecimal amount);
    void registerExpense(BigDecimal amount);

}
