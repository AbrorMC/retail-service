package uz.uzumtech.retail_service.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record FoodDetailsResponse(
        Long id,
        String name,
        String category,
        BigDecimal price,
        List<ReceiptItem> receipt
) {
    public record ReceiptItem(
            String ingredient,
            String measure,
            BigDecimal quantity
    ) {}
}
