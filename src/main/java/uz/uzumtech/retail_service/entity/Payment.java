package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    @Column(name = "reference_id", nullable = false, unique = true)
    Long referenceId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    TransactionType type;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    PaymentStatus status;

    @NotNull(message = "amount is required") @Positive(message = "amount should be positive")
    BigDecimal amount;

    @NotNull(message = "currency required")
    Currency currency;

    @NotBlank(message = "senderName required")
    @Column(name = "sender_name", nullable = false)
    String senderName;

    @NotBlank(message = "senderToken required")
    @Column(name = "sender_token", nullable = false)
    String senderToken;

    @NotBlank(message = "receiverName required")
    @Column(name = "receiver_name", nullable = false)
    String receiverName;

    @NotBlank(message = "receiverToken required")
    @Column(name = "receiver_token", nullable = false)
    String receiverToken;

}
