package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @PositiveOrZero
    @Column(precision = 19, scale = 2)
    BigDecimal totalAmount;

    @PositiveOrZero
    Integer itemCount;

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);

        if (this.itemCount == null) this.itemCount = 0;
        this.itemCount++;

        if (this.totalAmount == null) this.totalAmount = BigDecimal.ZERO;

        BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getCount()));
        this.totalAmount = this.totalAmount.add(itemTotal);

        item.setCart(this);
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item)) {
            this.itemCount--;

            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getCount()));

            if (this.totalAmount != null) {
                this.totalAmount = this.totalAmount.subtract(itemTotal);
            }

            item.setCart(null);
        }
    }

    public void removeAllItems() {
        for (OrderItem item : items) {
            item.setCart(null);
        }
        items.clear();

        this.itemCount = 0;
        this.totalAmount = BigDecimal.ZERO;
    }
}
