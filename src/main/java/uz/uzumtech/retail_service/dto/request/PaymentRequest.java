package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.uzumtech.retail_service.constant.enums.Currency;
import uz.uzumtech.retail_service.constant.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        @NotNull(message = "referenceId required")
        Long referenceId,

        @NotNull(message = "transaction type required")
        TransactionType type,

        @NotNull(message = "amount is required")
        @Positive(message = "amount should be positive")
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
        String receiverToken,

        @NotBlank(message = "receiverToken required")
        UUID merchantId
) {
}
