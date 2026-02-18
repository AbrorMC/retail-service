package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Finance;

public interface FinanceRepository extends JpaRepository<Finance, Long> {
}
