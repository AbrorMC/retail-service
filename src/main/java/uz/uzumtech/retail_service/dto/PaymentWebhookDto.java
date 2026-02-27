package uz.uzumtech.retail_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import uz.uzumtech.retail_service.constant.enums.Currency;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentWebhookDto(
        @NotNull(message = "id is required")
        Long id,

        @NotNull(message = "referenceId is required")
        Long referenceId,

        @NotNull(message = "status is required")
        PaymentStatus status,

        @NotNull(message = "amount is required")
        @Positive(message = "amount must be positive")
        BigDecimal amount,

        @NotNull(message = "currency is required")
        Currency currency,

        @NotNull(message = "createdAt is required")
        @PastOrPresent(message = "createdAt cannot be in the future")
        OffsetDateTime createdAt
) {}
