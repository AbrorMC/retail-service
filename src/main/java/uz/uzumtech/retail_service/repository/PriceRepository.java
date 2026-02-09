package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Price;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findByFoodIdAndIsActiveTrue(Long foodId);

}
