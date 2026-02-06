package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.mapper.CategoryMapper;
import uz.uzumtech.retail_service.repository.CategoryRepository;
import uz.uzumtech.retail_service.service.CategoryService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAll(int pageNumber, int size) {
        var pageable = PaginationValidator.validate(pageNumber, size);

        return categoryRepository
                .findAll(pageable)
                .map(categoryMapper::toResponse);
    }

}
