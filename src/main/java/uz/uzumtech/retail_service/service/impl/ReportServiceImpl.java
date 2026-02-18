package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.FinancialState;
import uz.uzumtech.retail_service.entity.Finance;
import uz.uzumtech.retail_service.repository.FinanceRepository;
import uz.uzumtech.retail_service.service.ReportService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {

    FinanceRepository financeRepository;

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
}
