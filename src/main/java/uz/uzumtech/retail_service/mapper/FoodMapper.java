package uz.uzumtech.retail_service.mapper;

import org.mapstruct.*;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.entity.Food;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodResponse toResponse(Food food);

}