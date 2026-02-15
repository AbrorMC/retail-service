package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Currency;

public record PaymentRequest(
        @NotNull(message = "referenceId required")
        Long referenceId,

        @NotNull(message = "transaction type required")
        String type,

        @NotNull(message = "amount is required") @Positive(message = "amount should be positive")
        BigDecimal amount,

        @NotNull(message = "currency required")
        Currency currency,

        @NotBlank(message = "senderName required")
        String senderName,

        @NotBlank(message = "senderToken required")
        String senderToken,

        @NotBlank(message = "receiverName required")
        String receiverName,

        @NotBlank(message = "receiverToken required")
        String receiverToken
) {
}
