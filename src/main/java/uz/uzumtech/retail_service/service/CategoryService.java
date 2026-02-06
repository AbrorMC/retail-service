package uz.uzumtech.retail_service.service;

import org.springframework.data.domain.Pageable;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface CategoryService {

    PageResponse<CategoryResponse> getAll(Pageable request);

}
