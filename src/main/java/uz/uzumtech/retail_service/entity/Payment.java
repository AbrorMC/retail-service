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
import uz.uzumtech.retail_service.constant.enums.Currency;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.constant.enums.TransactionType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "reference_id", nullable = false, unique = true, updatable = false)
    Long referenceId;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    TransactionType type;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    PaymentStatus status;

    @NotNull(message = "amount is required")
    @Positive(message = "amount should be positive")
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    BigDecimal amount;

    @NotNull(message = "currency required")
    @Column(nullable = false)
    Currency currency;

    @NotBlank(message = "senderName required")
    @Column(name = "sender_name", nullable = false, length = 255)
    String senderName;

    @NotBlank(message = "senderToken required")
    @Column(name = "sender_token", nullable = false, length = 512) // Токены часто длинные
    String senderToken;

    @NotBlank(message = "receiverName required")
    @Column(name = "receiver_name", nullable = false, length = 255)
    String receiverName;

    @NotBlank(message = "receiverToken required")
    @Column(name = "receiver_token", nullable = false, length = 512)
    String receiverToken;
}
