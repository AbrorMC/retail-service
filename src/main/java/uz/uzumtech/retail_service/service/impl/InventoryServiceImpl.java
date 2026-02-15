package uz.uzumtech.retail_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.uzumtech.retail_service.entity.Ingredient;
import uz.uzumtech.retail_service.entity.Inventory;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.repository.InventoryRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.service.InventoryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    OrderItemRepository orderItemRepository;

    @Override
    public boolean reserveItems(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository
                .findByOrderId(orderId)
                .orElseThrow();

        List<Inventory> inventoryItems = new ArrayList<>();

        orderItems.forEach(orderItem -> {

                    var orderQuantity = BigDecimal.valueOf(orderItem.getCount());

                    orderItem.getFood().getReceipt().forEach(ingredient -> {
                        var inventory = inventoryRepository
                                .findByIngredientId(ingredient.getId())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found in inventory: " + ingredient.getId()));

                        var totalRequired = ingredient.getQuantity().multiply(orderQuantity);

                        inventory.setQuantity(inventory.getQuantity().subtract(totalRequired));

                        inventoryItems.add(inventory);
                    });
                });

        return true;
    }
}
