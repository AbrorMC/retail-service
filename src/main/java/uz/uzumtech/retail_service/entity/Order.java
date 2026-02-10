package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(name = "total_price", precision = 19, scale = 2)
    BigDecimal totalPrice;

    @Positive
    Integer itemCount;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    OrderStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    private void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        item.setCart(null);
    }

    public void addAll(List<OrderItem> orderItems) {
        orderItems.forEach(this::addItem);
    }

    public void removeAllItems() {
        for (OrderItem item : items) {
            item.setOrder(null);
        }
        items.clear();

        this.itemCount = 0;
        this.totalPrice = BigDecimal.ZERO;
    }
}
