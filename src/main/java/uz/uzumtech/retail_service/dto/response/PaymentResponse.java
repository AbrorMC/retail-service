package uz.uzumtech.retail_service.dto.response;

import uz.uzumtech.retail_service.constant.enums.Currency;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        Long id,
        Long referenceId,
        PaymentStatus status,
        BigDecimal amount,
        Currency currency,
        OffsetDateTime createdAt
) {
}
