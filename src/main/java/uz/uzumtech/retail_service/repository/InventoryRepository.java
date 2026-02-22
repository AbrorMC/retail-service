package uz.uzumtech.retail_service.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.entity.Inventory;

import java.util.List;
import java.util.Map;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT inv
            FROM Inventory AS inv
            WHERE inv.id IN (
                SELECT
                    MAX(inv2.id)
                FROM Inventory AS inv2
                JOIN ReceiptItem AS ri ON ri.ingredient.id = inv2.ingredient.id
                JOIN OrderItem AS oi ON oi.food.id = ri.food.id
                WHERE oi.order.id = :orderId
                GROUP BY inv2.ingredient.id
            )
        """)
    List<Inventory> lockAndGetInventories(@Param("orderId") Long orderId);
}