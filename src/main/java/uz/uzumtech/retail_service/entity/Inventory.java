package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.SqlTypedJdbcType;
import uz.uzumtech.retail_service.constant.InventoryTransactionType;

import java.math.BigDecimal;
import java.sql.SQLType;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    Ingredient ingredient;

    @Positive
    @Column(precision = 19, scale = 2, nullable = false)
    BigDecimal quantity;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    InventoryTransactionType type;

    @Positive
    @Column(name = "actual_stock", precision = 19, scale = 2, nullable = false)
    BigDecimal actualStock;

    @Positive
    @Column(name = "total_cost", precision = 19, scale = 2, nullable = false)
    BigDecimal totalCost;

    @Version
    Long version;
}
