package uz.uzumtech.retail_service.dto.response;

import uz.uzumtech.retail_service.constant.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        String userId,
        OrderStatus status,
        Integer itemCount,
        BigDecimal totalPrice,
        String createdAt,
        String updatedAt
) {}
