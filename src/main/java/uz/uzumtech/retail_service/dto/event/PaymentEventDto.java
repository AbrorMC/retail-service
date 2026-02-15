package uz.uzumtech.retail_service.dto.event;

public record PaymentEventDto(
        String key,
        String correlationId,
        String message
) {
}
