package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.entity.Cart;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class}
)
public interface CartMapper extends BaseMapper<CartResponse, Cart> {

    CartResponse toResponse(Cart cart);
}
