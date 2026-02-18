package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.constant.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {
    //TODO: update annotations

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "referenceId required")
    Long referenceId;

    @NotNull(message = "transaction type required")
    TransactionType type;

    PaymentStatus status;

    @NotNull(message = "amount is required") @Positive(message = "amount should be positive")
    BigDecimal amount;

    @NotNull(message = "currency required")
    Currency currency;

    @NotBlank(message = "senderName required")
    String senderName;

    @NotBlank(message = "senderToken required")
    String senderToken;

    @NotBlank(message = "receiverName required")
    String receiverName;

    @NotBlank(message = "receiverToken required")
    String receiverToken;

}
