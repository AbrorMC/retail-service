package uz.uzumtech.retail_service.dto.response;

import java.util.List;

public record FoodDetailsResponse(
        Long id,
        String name,
        String category,
        List<ReceiptItemResponse> receipt
) {}
