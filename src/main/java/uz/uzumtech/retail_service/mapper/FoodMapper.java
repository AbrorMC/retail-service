package uz.uzumtech.retail_service.mapper;

import org.mapstruct.*;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.entity.Food;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ReceiptItemMapper.class}
)
public interface FoodMapper extends BaseMapper<FoodResponse, Food>{

    FoodResponse toResponse(Food food);

    @Mapping(target = "category", source = "category.name")
    FoodDetailsResponse toDetailedResponse(Food food);

}