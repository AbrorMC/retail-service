package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.entity.Order;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class}
)
public interface OrderMapper extends BaseMapper<OrderResponse, Order> {

    OrderResponse toResponse(Order order);
    Order toEntity(OrderRequest request);
}
