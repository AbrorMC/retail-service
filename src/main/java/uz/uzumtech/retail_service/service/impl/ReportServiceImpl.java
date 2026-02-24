package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.FinancialState;
import uz.uzumtech.retail_service.dto.projection.InventoryStock;
import uz.uzumtech.retail_service.dto.request.PeriodFilterRequest;
import uz.uzumtech.retail_service.dto.response.FinancialResponse;
import uz.uzumtech.retail_service.entity.Finance;
import uz.uzumtech.retail_service.repository.FinanceRepository;
import uz.uzumtech.retail_service.repository.InventoryRepository;
import uz.uzumtech.retail_service.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {

    FinanceRepository financeRepository;
    InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void registerIncome(BigDecimal amount) {
        financeRepository.save(Finance.builder()
                .state(FinancialState.INCOME)
                .amount(amount)
                .build());
    }

    @Override
    @Transactional
    public void registerExpense(BigDecimal amount) {
        financeRepository.save(Finance.builder()
                .state(FinancialState.EXPENSE)
                .amount(amount)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialResponse getFinancialReport(PeriodFilterRequest request) {
        LocalDateTime start = request.startDate().atStartOfDay();
        LocalDateTime end = request.endDate().atTime(LocalTime.MAX);

        var finances = financeRepository.get(start, end);

        BigDecimal totalIncome = finances.getTotalIncome() != null ?
                finances.getTotalIncome() : BigDecimal.ZERO;
        BigDecimal totalExpense = finances.getTotalExpense() != null ?
                finances.getTotalExpense() : BigDecimal.ZERO;
        BigDecimal netProfit = totalIncome.subtract(totalExpense);

        return new FinancialResponse(totalIncome, totalExpense, netProfit);
    }

    @Override
    public List<InventoryStock> getStockReport(LocalDate date) {

        LocalDateTime dateTime = Objects.equals(date, LocalDate.now()) ?
                LocalDateTime.now() : date.atTime(LocalTime.MAX);

        return inventoryRepository
                .getInventoriesToDate(dateTime);
    }
}
