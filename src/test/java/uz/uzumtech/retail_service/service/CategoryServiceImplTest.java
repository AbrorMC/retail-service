package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Category;
import uz.uzumtech.retail_service.mapper.CategoryMapper;
import uz.uzumtech.retail_service.repository.CategoryRepository;
import uz.uzumtech.retail_service.service.impl.CategoryServiceImpl;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Получение всех категорий")
    void getAll_ShouldReturnPageResponse_WhenCategoriesExist() {
        // Arrange
        int pageNumber = 1;
        int size = 10;

        var pageable = PaginationValidator.validate(pageNumber, size);
        var page = new PageImpl<>(List.of(new Category()));

        var expectedResponse = new PageResponse<>(
                List.of(new CategoryResponse(1L, "Салаты", "Салаты")),
                1,
                1,
                1,
                true
        );

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toPageResponse(page)).thenReturn(expectedResponse);

        // Act
        var actualResponse = categoryService.getAll(pageNumber, size);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify
        verify(categoryRepository).findAll(any(Pageable.class));
        verify(categoryMapper).toPageResponse(page);
    }
}
