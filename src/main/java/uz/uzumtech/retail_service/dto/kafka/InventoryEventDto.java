package uz.uzumtech.retail_service.dto.kafka;

import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.dto.response.OrderResponse;

import java.math.BigDecimal;

public record InventoryEventDto(
        String key,
        String correlationId,
        InventoryEventMessage message
) {
    public record InventoryEventMessage(
            EventStatus status,
            BigDecimal income,
            BigDecimal expense
    ) {
    }
}
