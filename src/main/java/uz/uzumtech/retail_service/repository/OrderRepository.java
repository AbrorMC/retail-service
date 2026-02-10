package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
