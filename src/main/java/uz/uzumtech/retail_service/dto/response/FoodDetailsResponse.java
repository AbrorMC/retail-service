package uz.uzumtech.retail_service.dto.response;

import uz.uzumtech.retail_service.constant.enums.FoodAvailability;

import java.math.BigDecimal;
import java.util.List;

public record FoodDetailsResponse(
        Long id,
        String name,
        String category,
        List<ReceiptItemResponse> receipt,
        String status,
        BigDecimal price
) {}
