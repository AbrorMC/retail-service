package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import uz.uzumtech.retail_service.constant.enums.FinancialState;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "finances")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Finance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    FinancialState state;

    @Positive
    @Column(precision = 19, scale = 2, nullable = false)
    BigDecimal amount;
}
