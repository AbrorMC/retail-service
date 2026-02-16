package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.Order;
import uz.uzumtech.retail_service.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<Cart> findByCartId(Long id);

    @Query(value = """
    SELECT COUNT(DISTINCT ri.ingredient_id)
    FROM order_items oi
    JOIN receipt_items ri ON oi.food_id = ri.food_id
    WHERE oi.order_id = :orderId
    """, nativeQuery = true)
    long countUniqueIngredientsByOrderId(@Param("orderId") Long orderId);
}
