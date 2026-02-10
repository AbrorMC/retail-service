package uz.uzumtech.retail_service.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Integer itemCount,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {}
