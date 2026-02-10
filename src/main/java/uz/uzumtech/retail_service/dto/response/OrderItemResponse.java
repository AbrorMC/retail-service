package uz.uzumtech.retail_service.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String food,
        Integer count,
        BigDecimal price,
        BigDecimal total
) {
}
