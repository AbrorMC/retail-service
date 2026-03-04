package uz.uzumtech.retail_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.uzumtech.retail_service.dto.MaterialsReportDto;
import uz.uzumtech.retail_service.dto.projection.InventoryStock;
import uz.uzumtech.retail_service.dto.request.PeriodFilterRequest;
import uz.uzumtech.retail_service.dto.response.FinancialResponse;
import uz.uzumtech.retail_service.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/retail/reports")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @GetMapping("/financial")
    public ResponseEntity<FinancialResponse> getFinancialReport(@Valid PeriodFilterRequest request) {
        return ResponseEntity.ok(reportService.getFinancialReport(request));
    }

    @GetMapping("/stock")
    public ResponseEntity<List<InventoryStock>> getStockReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getStockReport(date));
    }

    @GetMapping("/materials")
    public ResponseEntity<List<MaterialsReportDto>> getMaterialsReport(@Valid PeriodFilterRequest request) {
        return ResponseEntity.ok(reportService.getMaterialsReport(request));
    }
}
