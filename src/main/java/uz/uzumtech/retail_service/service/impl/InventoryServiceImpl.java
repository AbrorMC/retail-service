package uz.uzumtech.retail_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.exception.InsufficientStockException;
import uz.uzumtech.retail_service.repository.InventoryRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.service.InventoryService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public EventStatus consumeIngredients(Long orderId) {

        long expectedCount = orderItemRepository.countUniqueIngredientsByOrderId(orderId);
        int updatedRows = inventoryRepository.writeOffInventory(orderId);

        if (updatedRows == (int) expectedCount) {
            return EventStatus.INVENTORY_RESERVED;
        } else {
            TransactionAspectSupport
                    .currentTransactionStatus()
                    .setRollbackOnly();

            return EventStatus.OUT_OF_STOCK;
        }
    }

    @Override
    public BigDecimal getIncome() {
        //TODO: implement income calculation logic
        return BigDecimal.ONE;
    }

    @Override
    public BigDecimal getExpense() {
        //TODO: implement expense calculation logic
        return BigDecimal.ONE;
    }
}
