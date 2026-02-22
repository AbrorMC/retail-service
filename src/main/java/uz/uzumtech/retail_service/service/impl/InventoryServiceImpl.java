package uz.uzumtech.retail_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.uzumtech.retail_service.constant.InventoryTransactionType;
import uz.uzumtech.retail_service.entity.Inventory;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.repository.IngredientRequirement;
import uz.uzumtech.retail_service.repository.InventoryRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.InventoryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;

    @Override
    @Transactional
    public BigDecimal consumeIngredients(Long orderId) {

        Map<Long, Inventory> inventories = inventoryRepository
                .lockAndGetInventories(orderId)
                .stream()
                .collect(
                        Collectors.toMap(
                        inv -> inv.getIngredient().getId(),
                        inv -> inv)
                );

        List<IngredientRequirement> neededIngredients = orderItemRepository
                .getNeededIngredientsByOrderId(orderId);

        List<Inventory> newRecords = new ArrayList<>();

        for (var neededIngredient : neededIngredients) {
            Long ingredientId = neededIngredient.getIngredientId();
            BigDecimal neededQuantity = neededIngredient.getTotalQuantity();

            var inventory = inventories.get(ingredientId);

            if (inventory == null || inventory.getActualStock().compareTo(neededQuantity) < 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return BigDecimal.ZERO;
            }

            BigDecimal inventoryTotalCost = inventory.getTotalCost()
                    .divide(inventory.getQuantity(), RoundingMode.CEILING)
                    .multiply(neededQuantity);

            var record = Inventory.builder()
                    .ingredient(inventory.getIngredient())
                    .type(InventoryTransactionType.WRITE_OFF)
                    .quantity(neededQuantity)
                    .actualStock(inventory.getActualStock().subtract(neededQuantity))
                    .totalCost(inventoryTotalCost)
                    .createdAt(LocalDateTime.now())
                    .build();

            newRecords.add(record);
        }

        inventoryRepository.saveAll(newRecords);

        return newRecords.stream()
                .map(Inventory::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getIncome(Long orderId) {
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        return order.getTotalPrice();
    }
}
