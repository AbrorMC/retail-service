package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.entity.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper extends BaseMapper<OrderItemResponse, OrderItem> {

    OrderItem toEntity(OrderItemRequest request);

    @Mapping(target = "food", source = "food.name")
    @Mapping(target = "count", source = "count")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "total", expression = "java(price.multiply(BigDecimal.valueOf(count)))")
    OrderItemResponse toResponse(OrderItem orderItem);

}
