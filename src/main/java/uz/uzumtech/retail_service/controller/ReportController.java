package uz.uzumtech.retail_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.uzumtech.retail_service.dto.request.ReportFilterRequest;
import uz.uzumtech.retail_service.dto.response.FinancialResponse;
import uz.uzumtech.retail_service.service.ReportService;

@RestController
@RequestMapping("api/core/reports")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @GetMapping("/financial")
    public ResponseEntity<FinancialResponse> getFinancialReport(@Valid ReportFilterRequest request) {
        return ResponseEntity.ok(reportService.getFinancialReport(request));
    }


}
