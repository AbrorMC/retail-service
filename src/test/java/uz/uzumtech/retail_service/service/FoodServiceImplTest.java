package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uz.uzumtech.retail_service.constant.enums.FoodAvailability;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Category;
import uz.uzumtech.retail_service.entity.Food;
import uz.uzumtech.retail_service.entity.Price;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.exception.PriceNotFoundException;
import uz.uzumtech.retail_service.mapper.FoodMapper;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.repository.PriceRepository;
import uz.uzumtech.retail_service.service.impl.FoodServiceImpl;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepository;
    @Mock
    PriceRepository priceRepository;
    @Mock
    private FoodMapper foodMapper;

    @InjectMocks
    private FoodServiceImpl foodService;

    private final Long foodId = 1L;

    @Test
    @DisplayName("Получение блюд из категории")
    void getByCategoryId_ShouldReturnPageResponse_WhenCategoryFoodsExist() {
        // Arrange
        int pageNumber = 1;
        int size = 10;
        Long categoryId = 1L;

        var pageable = PaginationValidator.validate(pageNumber, size);
        var page = new PageImpl<>(List.of(new Food()));

        var expectedResponse = new PageResponse<>(
                List.of(new FoodResponse(foodId, "Плов")),
                1,
                1,
                1,
                true
        );

        when(foodRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(page);
        when(foodMapper.toPageResponse(page)).thenReturn(expectedResponse);

        // Act
        var actualResponse = foodService.getByCategoryId(categoryId, pageNumber, size);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify
        verify(foodRepository).findAllByCategoryId(any(), any(Pageable.class));
        verify(foodMapper).toPageResponse(page);
    }

    @Test
    @DisplayName("Получение деталей блюда")
    void get_ShouldReturnResponse_WhenFoodAndPriceExists() {
        // Arrange
        Food food = new Food();
        food.setName("Плов");

        Category category = new Category();
        category.setName("Горячее");
        food.setCategory(category);

        Price price = new Price();
        BigDecimal expectedPrice = new BigDecimal("150.00");
        price.setPrice(expectedPrice);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(priceRepository.findByFoodIdAndIsActiveTrue(foodId)).thenReturn(Optional.of(price));
        when(foodRepository.getAvailableServings(foodId)).thenReturn(10);

        FoodDetailsResponse expectedResponse = new FoodDetailsResponse(
                foodId, "Плов", "Горячее", List.of(), "AVAILABLE", expectedPrice
        );

        when(foodMapper.toDetailedResponse(eq(food), eq(FoodAvailability.AVAILABLE), eq(price.getPrice())))
                .thenReturn(expectedResponse);

        // Act
        FoodDetailsResponse actualResponse = foodService.get(foodId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedPrice, actualResponse.price());
        assertEquals("AVAILABLE", actualResponse.status());

        // Verify
        verify(foodRepository).findById(foodId);
        verify(priceRepository).findByFoodIdAndIsActiveTrue(foodId);
    }

    @ParameterizedTest
    @DisplayName("Определения статуса доступности блюда")
    @CsvSource({
            "0, NOT_AVAILABLE",
            "1, ONE",
            "2, TWO",
            "3, ENDING",
            "4, ENDING",
            "5, ENDING",
            "6, AVAILABLE"
    })
    void get_ShouldPassCorrectAvailabilityToMapper(int servings, FoodAvailability expectedEnum) {
        // Arrange
        when(foodRepository.findById(any())).thenReturn(Optional.of(new Food()));
        when(priceRepository.findByFoodIdAndIsActiveTrue(any())).thenReturn(Optional.of(new Price()));
        when(foodRepository.getAvailableServings(any())).thenReturn(servings);

        // Act
        foodService.get(foodId);

        // Verify
        verify(foodMapper).toDetailedResponse(any(), eq(expectedEnum), any());
    }

    @Test
    @DisplayName("Еда не найдена")
    void get_ShouldThrowFoodNotFoundException_WhenFoodDoesNotExist() {
        // Arrange
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Assert
        assertThrows(FoodNotFoundException.class, () -> foodService.get(foodId));

        // Verify
        verifyNoInteractions(priceRepository, foodMapper);
    }

    @Test
    @DisplayName("Цена не найдена")
    void get_ShouldThrowPriceNotFoundException_WhenPriceDoesNotExist() {
        // Arrange
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(new Food()));
        when(priceRepository.findByFoodIdAndIsActiveTrue(foodId)).thenReturn(Optional.empty());

        // Act
        assertThrows(PriceNotFoundException.class, () -> foodService.get(foodId));

        // Verify
        verify(foodRepository).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }
}
