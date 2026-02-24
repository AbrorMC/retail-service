package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.dto.projection.FinanceSummary;
import uz.uzumtech.retail_service.entity.Finance;

import java.time.LocalDateTime;
import java.util.List;

public interface FinanceRepository extends JpaRepository<Finance, Long> {

    @Query("""
        SELECT
            SUM(CASE WHEN state = 'INCOME' THEN amount ELSE 0 END) AS totalIncome,
            SUM(CASE WHEN state = 'EXPENSE' THEN amount ELSE 0 END) AS totalExpense
        FROM Finance
        WHERE createdAt BETWEEN :start AND :end
    """)
    FinanceSummary get(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
