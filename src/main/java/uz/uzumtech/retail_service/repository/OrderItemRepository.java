package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.OrderItem;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<Cart> findByCartId(Long id);
}
