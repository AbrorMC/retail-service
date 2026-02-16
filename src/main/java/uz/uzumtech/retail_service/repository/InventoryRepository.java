package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Modifying
    @Query(value = """
        UPDATE inventories inv
        SET quantity = inv.quantity - required.needed
        FROM (
            SELECT ri.ingredient_id, SUM(oi.count * ri.quantity) as needed
            FROM order_items oi
            JOIN receipt_items ri ON oi.food_id = ri.food_id
            WHERE oi.order_id = :orderId
            GROUP BY ri.ingredient_id
        ) AS required
        WHERE inv.ingredient_id = required.ingredient_id
          AND inv.quantity >= required.needed
        """, nativeQuery = true)
    int writeOffInventory(@Param("orderId") Long orderId);
}