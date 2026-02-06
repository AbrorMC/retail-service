package uz.uzumtech.retail_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;

public interface CategoryService {

    Page<CategoryResponse> getAll(int page, int size);

}
