package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<Cart> findByCartId(Long id);

    @Query("""
        SELECT
            ri.ingredient.id AS ingredientId,
            SUM(oi.count * ri.quantity) AS totalQuantity
        FROM OrderItem oi
        JOIN ReceiptItem ri ON oi.food.id = ri.food.id
        WHERE oi.order.id = :orderId
        GROUP BY ri.ingredient.id
    """)
    List<IngredientRequirement> getNeededIngredientsByOrderId(@Param("orderId") Long orderId);
}
