package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.CartItem;
import uz.uzumtech.retail_service.entity.Category;

@Mapper(componentModel = "spring")
public interface CartItemMapper extends BaseMapper<CartItemResponse, CartItem> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "food", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItem toEntity(CartItemRequest request);

    @Mapping(target = "food", source = "food.name")
    @Mapping(target = "count", source = "count")
    CartItemResponse toResponse(CartItem cartItem);
}
