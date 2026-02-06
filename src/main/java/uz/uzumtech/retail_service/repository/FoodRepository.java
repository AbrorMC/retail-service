package uz.uzumtech.retail_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findAllByCategoryId(Long id, Pageable page);
}
