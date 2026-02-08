package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
