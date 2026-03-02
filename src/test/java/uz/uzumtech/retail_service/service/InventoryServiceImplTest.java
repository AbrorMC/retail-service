package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import uz.uzumtech.retail_service.constant.enums.InventoryTransactionType;
import uz.uzumtech.retail_service.dto.projection.IngredientRequirement;
import uz.uzumtech.retail_service.entity.Ingredient;
import uz.uzumtech.retail_service.entity.Inventory;
import uz.uzumtech.retail_service.entity.Order;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.repository.InventoryRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.impl.InventoryServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private final Long ORDER_ID = 100L;
    private final Long INGREDIENT_ID = 1L;

    @Test
    @DisplayName("Успешное списание ингредиентов и правильный расчет себестоимости")
    void consumeIngredients_Success_ShouldCalculateCorrectCost() {
        // Arrange
        Ingredient ingredient = Ingredient.builder().id(INGREDIENT_ID).name("Рис").build();

        Inventory currentStock = Inventory.builder()
                .ingredient(ingredient)
                .quantity(new BigDecimal("10"))
                .actualStock(new BigDecimal("10"))
                .totalCost(new BigDecimal("1000"))
                .build();

        when(inventoryRepository.lockAndGetInventories(ORDER_ID))
                .thenReturn(List.of(currentStock));

        IngredientRequirement requirement = mock(IngredientRequirement.class);
        when(requirement.getIngredientId()).thenReturn(INGREDIENT_ID);
        when(requirement.getTotalQuantity()).thenReturn(new BigDecimal("2.5"));

        when(orderItemRepository.getNeededIngredientsByOrderId(ORDER_ID))
                .thenReturn(List.of(requirement));

        // Act
        BigDecimal totalCost = inventoryService.consumeIngredients(ORDER_ID);

        // Assert
        assertEquals(0, new BigDecimal("250").compareTo(totalCost));

        ArgumentCaptor<List<Inventory>> captor = ArgumentCaptor.forClass(List.class);
        verify(inventoryRepository).saveAll(captor.capture());

        Inventory savedRecord = captor.getValue().getFirst();
        assertEquals(InventoryTransactionType.WRITE_OFF, savedRecord.getType());
        assertEquals(0, new BigDecimal("7.5").compareTo(savedRecord.getActualStock()));
        assertEquals(0, new BigDecimal("750").compareTo(savedRecord.getTotalCost()));
    }

    @Test
    @DisplayName("Недостаточно ингредиентов на складе - должна произойти откат транзакции")
    void consumeIngredients_InsufficientStock_ShouldRollback() {
        try (MockedStatic<TransactionAspectSupport> txMock = mockStatic(TransactionAspectSupport.class)) {
            // Arrange
            TransactionStatus status = mock(TransactionStatus.class);
            txMock.when(TransactionAspectSupport::currentTransactionStatus).thenReturn(status);

            Inventory lowStock = Inventory.builder()
                    .ingredient(Ingredient.builder().id(INGREDIENT_ID).build())
                    .actualStock(new BigDecimal("1"))
                    .build();

            when(inventoryRepository.lockAndGetInventories(ORDER_ID)).thenReturn(List.of(lowStock));

            IngredientRequirement req = mock(IngredientRequirement.class);
            when(req.getIngredientId()).thenReturn(INGREDIENT_ID);
            when(req.getTotalQuantity()).thenReturn(new BigDecimal("5"));

            when(orderItemRepository.getNeededIngredientsByOrderId(ORDER_ID)).thenReturn(List.of(req));

            // Act
            BigDecimal result = inventoryService.consumeIngredients(ORDER_ID);

            // Assert
            assertEquals(BigDecimal.ZERO, result);

            // Verify
            verify(status).setRollbackOnly();
            verify(inventoryRepository, never()).saveAll(any());
        }
    }

    @Test
    @DisplayName("Получение дохода по заказу")
    void getIncome_ShouldReturnTotalPrice_WhenOrderExists() {
        // Arrange
        BigDecimal expectedPrice = new BigDecimal("100.00");
        Order order = new Order();
        order.setTotalPrice(expectedPrice);

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        // Act
        BigDecimal actualPrice = inventoryService.getIncome(ORDER_ID);

        // Assert
        assertNotNull(actualPrice);
        assertEquals(0, expectedPrice.compareTo(actualPrice));

        // Verify
        verify(orderRepository).findById(ORDER_ID);
    }

    @Test
    @DisplayName("Получение дохода по несуществующему заказу - должно выбросить исключение")
    void getIncome_ShouldThrowOrderNotFoundException_WhenOrderDoesNotExist() {
        // Arrange
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> inventoryService.getIncome(ORDER_ID)
        );
    }
}
