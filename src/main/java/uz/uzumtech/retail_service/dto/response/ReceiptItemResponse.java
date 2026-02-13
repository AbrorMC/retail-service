package uz.uzumtech.retail_service.dto.response;

import java.math.BigDecimal;

public record ReceiptItemResponse(
        String ingredient,
        BigDecimal quantity
) {
}
