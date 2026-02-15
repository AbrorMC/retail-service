package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.Order;
import uz.uzumtech.retail_service.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<Cart> findByCartId(Long id);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :id AND oi.order.status = 'CREATED'")
    Optional<List<OrderItem>> findByOrderId(Long id);
}
