package uz.uzumtech.retail_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.retail_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
