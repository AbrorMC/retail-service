package uz.uzumtech.retail_service.dto;

public record KafkaMessageDto(
        String key,
        String correlationId,
        String message
) {
}
