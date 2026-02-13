package uz.uzumtech.retail_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "foods")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Название блюда не может быть пустым!")
    @Column(nullable = false)
    String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Builder.Default
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReceiptItem> receipt = new ArrayList<>();

    public void addReceiptItem(ReceiptItem receiptItem) {
        receipt.add(receiptItem);
        receiptItem.setFood(this);
    }

    public void addAllReceiptItems(List<ReceiptItem> receiptItems) {
        receiptItems.forEach(this::addReceiptItem);
    }

    public void removeReceiptItem(ReceiptItem receiptItem) {
        receipt.remove(receiptItem);
        receiptItem.setFood(null);
    }
}
