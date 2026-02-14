package uz.uzumtech.retail_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findAllByCategoryId(Long id, Pageable pageable);

    @Query(value = """
            SELECT NOT EXISTS (
                  SELECT 1 FROM receipt_items ri
                  JOIN inventories i ON ri.ingredient_id = i.ingredient_id
                  WHERE ri.food_id = :food_id AND i.quantity < ri.quantity
              )
            """, nativeQuery = true)
    boolean isFoodAvailable(@Param("food_id") Long id);
}
