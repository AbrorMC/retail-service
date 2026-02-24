package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.ReportFilterRequest;
import uz.uzumtech.retail_service.dto.response.FinancialResponse;

import java.math.BigDecimal;

public interface ReportService {

    void registerIncome(BigDecimal amount);
    void registerExpense(BigDecimal amount);

    FinancialResponse getFinancialReport(ReportFilterRequest request);

}
