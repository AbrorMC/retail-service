package uz.uzumtech.retail_service.dto.kafka;

import uz.uzumtech.retail_service.dto.PaymentWebhookDto;

public record PaymentEventDto(
        String key,
        String correlationId,
        PaymentWebhookDto message
) {
}
