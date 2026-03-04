package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.FinancialState;
import uz.uzumtech.retail_service.dto.MaterialsReportDto;
import uz.uzumtech.retail_service.dto.projection.InventoryStock;
import uz.uzumtech.retail_service.dto.projection.InventoryTurnover;
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
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {

    FinanceRepository financeRepository;
    InventoryRepository inventoryRepository;

    Executor myExecutor = Executors.newFixedThreadPool(10);

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
    @Transactional(readOnly = true)
    public List<InventoryStock> getStockReport(LocalDate date) {
        LocalDateTime dateTime = Objects.equals(date, LocalDate.now()) ?
                LocalDateTime.now() : date.atTime(LocalTime.MAX);

        return inventoryRepository
                .getInventoriesToDate(dateTime);
    }

    @Override
    public List<MaterialsReportDto> getMaterialsReport(PeriodFilterRequest request) {
        LocalDateTime start = request.startDate().atStartOfDay();
        LocalDateTime end = request.endDate().atTime(LocalTime.MAX);

        var stockAtStartFuture = CompletableFuture.supplyAsync(() ->
                inventoryRepository.getInventoriesToDate(start), myExecutor);
        var turnoverFuture = CompletableFuture.supplyAsync(() ->
                inventoryRepository.getInventoryTurnover(start, end), myExecutor);
        var stockAtEndFuture = CompletableFuture.supplyAsync(() ->
                inventoryRepository.getInventoriesToDate(end), myExecutor);

        CompletableFuture.allOf(stockAtStartFuture, turnoverFuture, stockAtEndFuture).join();

        var stockAtStartMap = stockAtStartFuture.join()
                .stream()
                .collect(Collectors.toMap(InventoryStock::getIngredientName, s -> s));
        var turnoverMap = turnoverFuture.join()
                .stream()
                .collect(Collectors.toMap(InventoryTurnover::getIngredientName, t -> t));
        var stockAtEndMap = stockAtEndFuture.join()
                .stream()
                .collect(Collectors.toMap(InventoryStock::getIngredientName, s -> s));

        var allNames = Stream.of(stockAtStartMap.keySet(), turnoverMap.keySet(), stockAtEndMap.keySet())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        return allNames
                .stream()
                .map(name -> new MaterialsReportDto(
                        name.toString(),
                        stockAtStartMap.get(name) != null ? stockAtStartMap.get(name).getUnit() : turnoverMap.get(name).getUnit(),
                        stockAtStartMap.get(name) != null ? stockAtStartMap.get(name).getActualStock() : BigDecimal.ZERO,
                        stockAtStartMap.get(name) != null ? stockAtStartMap.get(name).getTotalCost() : BigDecimal.ZERO,
                        turnoverMap.get(name) != null ? turnoverMap.get(name).getIncomeQuantity() : BigDecimal.ZERO,
                        turnoverMap.get(name) != null ? turnoverMap.get(name).getOutcomeQuantity() : BigDecimal.ZERO,
                        stockAtEndMap.get(name) != null ? stockAtEndMap.get(name).getActualStock() : BigDecimal.ZERO,
                        stockAtEndMap.get(name) != null ? stockAtEndMap.get(name).getTotalCost() : BigDecimal.ZERO

                ))
                .toList();
    }
}
