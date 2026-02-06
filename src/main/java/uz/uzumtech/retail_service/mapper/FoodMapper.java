package uz.uzumtech.retail_service.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Food;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodResponse toResponse(Food food);

    @Mapping(target = "category", source = "food.category.name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "receipt", source = "food.receipt.items")
    FoodDetailsResponse toDetailsResponse(Food food, BigDecimal price);

    default PageResponse<FoodResponse> toPageResponse(Page<Food> page) {
        return new PageResponse<>(
                page.getContent().stream().map(this::toResponse).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.isLast()
        );
    }

}