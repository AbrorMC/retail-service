package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.projection.InventoryStock;
import uz.uzumtech.retail_service.dto.MaterialsReportDto;
import uz.uzumtech.retail_service.dto.request.PeriodFilterRequest;
import uz.uzumtech.retail_service.dto.response.FinancialResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    void registerIncome(BigDecimal amount);
    void registerExpense(BigDecimal amount);

    FinancialResponse getFinancialReport(PeriodFilterRequest request);
    List<InventoryStock> getStockReport(LocalDate date);
    List<MaterialsReportDto> getMaterialsReport(PeriodFilterRequest request);
}
