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
                SELECT COALESCE(
                    (SELECT
                        COALESCE(BOOL_AND(inv.actual_stock - ri.quantity >= 0), false) is_available
                    FROM receipt_items ri
                    LEFT JOIN mv_inventory_balances inv ON ri.ingredient_id = inv.ingredient_id
                    WHERE ri.food_id = :foodId AND ri.is_active = TRUE),
                FALSE)
            """, nativeQuery = true)
    boolean isFoodAvailable(@Param("foodId") Long id);
}
