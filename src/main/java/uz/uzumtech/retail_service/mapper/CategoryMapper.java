package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<CategoryResponse, Category> {

    CategoryResponse toResponse(Category category);

}
