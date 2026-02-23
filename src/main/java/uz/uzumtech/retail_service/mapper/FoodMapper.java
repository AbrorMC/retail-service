package uz.uzumtech.retail_service.mapper;

import org.mapstruct.*;
import uz.uzumtech.retail_service.constant.enums.FoodAvailability;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.entity.Food;

import java.math.BigDecimal;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ReceiptItemMapper.class}
)
public interface FoodMapper extends BaseMapper<FoodResponse, Food>{

    FoodResponse toResponse(Food food);

    @Mapping(target = "category", source = "food.category.name")
    @Mapping(target = "status", source = "status.description")
    @Mapping(target = "price", source = "price")
    FoodDetailsResponse toDetailedResponse(Food food, FoodAvailability status, BigDecimal price);

}