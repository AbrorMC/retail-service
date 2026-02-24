package uz.uzumtech.retail_service.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.dto.projection.InventoryStock;
import uz.uzumtech.retail_service.dto.projection.MaterialsReport;
import uz.uzumtech.retail_service.entity.Inventory;

import java.time.LocalDateTime;
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

    @Query(value = """
                SELECT
                    name as ingredient_name,
                	unit,
                    actual_stock AS actualStock,
                    total_cost AS totalCost
                FROM (
                    SELECT
                        ing.name,
                		ing.unit,
                        inv.actual_stock,
                        inv.total_cost,
                        ROW_NUMBER() OVER (PARTITION BY inv.ingredient_id ORDER BY inv.id DESC) as rn
                    FROM inventories inv
                    LEFT JOIN ingredients ing ON ing.id = inv.ingredient_id
                    WHERE inv.created_at <= :date
                ) ranked_inv
                WHERE rn = 1;
            """, nativeQuery = true)
    List<InventoryStock> getInventoriesToDate(LocalDateTime date);

    @Query(value = """
                SELECT
                    ing.name AS ingredient_name,
                    ing.unit,
                    MAX(inv.actual_stock)
                        FILTER (WHERE inv.rn_start = 1) AS stock_begin,
                    MAX(inv.total_cost)
                        FILTER (WHERE inv.rn_start = 1) AS cost_begin,
                    COALESCE(SUM(inv.quantity)
                        FILTER (WHERE inv.created_at > :startDate
                                  AND inv.created_at <= :endDate
                                  AND inv.type = 'INCOMING'), 0) AS income_quantity,
                    COALESCE(SUM(inv.quantity)
                        FILTER (WHERE inv.created_at > :startDate
                                  AND inv.created_at <= :endDate
                                  AND inv.type = 'WRITE_OFF'), 0) AS outcome_quantity,
                    MAX(inv.actual_stock)
                        FILTER (WHERE inv.rn_end = 1) AS stock_end,
                    MAX(inv.total_cost)
                        FILTER (WHERE inv.rn_end = 1) AS cost_end
                FROM ingredients ing
                LEFT JOIN (
                    SELECT
                        ingredient_id,
                        actual_stock,
                        total_cost,
                        quantity,
                        type,
                        created_at,
                        ROW_NUMBER() OVER (
                            PARTITION BY ingredient_id
                            ORDER BY (CASE WHEN created_at <= :startDate THEN id ELSE -1 END) DESC
                        ) AS rn_start,
                        ROW_NUMBER() OVER (
                            PARTITION BY ingredient_id
                            ORDER BY (CASE WHEN created_at <= :endDate THEN id ELSE -1 END) DESC
                        ) AS rn_end
                    FROM inventories
                    WHERE created_at <= :endDate
                ) inv ON ing.id = inv.ingredient_id
                GROUP BY ing.id, ing.name, ing.unit;
            """, nativeQuery = true)
    List<MaterialsReport> getMaterialsReport(LocalDateTime startDate, LocalDateTime endDate);
}