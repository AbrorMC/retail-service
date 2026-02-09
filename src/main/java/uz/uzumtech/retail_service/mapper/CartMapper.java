package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.CartItem;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper extends BaseMapper<CartResponse, Cart> {

    CartResponse toResponse(Cart cart);
}
