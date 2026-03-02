package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.Food;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.entity.Price;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.exception.PriceNotFoundException;
import uz.uzumtech.retail_service.mapper.OrderItemMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.repository.PriceRepository;
import uz.uzumtech.retail_service.service.impl.CartServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private FoodRepository foodRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private PriceRepository priceRepository;
    @Mock
    private CartTransactionService cartTransactionService;
    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private OrderItemRequest request;
    private BigDecimal testPriceValue;

    @BeforeEach
    void setUp() {
        request = new OrderItemRequest(1L, 2L, 1);
        testPriceValue = BigDecimal.valueOf(100);
    }

    @Test
    @DisplayName("Успешное добавление")
    void addItem_Success() {
        // Arrange
        Cart cart = new Cart();
        Food food = new Food();
        food.setName("Плов");

        Price price = new Price();
        price.setPrice(testPriceValue);

        OrderItem cartItem = new OrderItem();
        cartItem.setFood(food);
        cartItem.setPrice(testPriceValue);
        cartItem.setCount(1);

        var expectedResponse = new OrderItemResponse(
                1L, "Плов", 1, testPriceValue, testPriceValue
        );

        when(foodRepository.getAvailableServings(request.foodId())).thenReturn(1);
        when(foodRepository.findById(request.foodId())).thenReturn(Optional.of(food));
        when(cartRepository.findById(request.cartId())).thenReturn(Optional.of(cart));
        when(priceRepository.findByFoodIdAndIsActiveTrue(request.foodId())).thenReturn(Optional.of(price));
        when(orderItemMapper.toEntity(request)).thenReturn(cartItem);
        when(orderItemMapper.toResponse(any())).thenReturn(expectedResponse);

        // Act
        var result = cartService.addItem(request);

        // Assert
        assertNotNull(result);
        assertEquals("Плов", result.food());
        assertTrue(cart.getItems().contains(cartItem));
        assertEquals(food, cartItem.getFood());
        assertEquals(testPriceValue, cartItem.getPrice());

        // Verify
        verify(cartTransactionService).saveCart(cart);
        verify(cartTransactionService).saveItem(cartItem);
    }

    @Test
    @DisplayName("Количество порций 0 или меньше")
    void addItem_ThrowsIllegalStateException_WhenNoServings() {
        // Arrange
        when(foodRepository.getAvailableServings(2L)).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cartService.addItem(request));

        // Verify
        verifyNoInteractions(cartRepository, priceRepository, cartTransactionService);
    }

    @Test
    @DisplayName("Еда не найдена")
    void addItem_ThrowsFoodNotFoundExceptions() {
        // Arrange
        when(foodRepository.getAvailableServings(anyLong())).thenReturn(10);
        when(foodRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FoodNotFoundException.class, () -> cartService.addItem(request));
    }

    @Test
    @DisplayName("Корзина не найдена")
    void addItem_ThrowsCartNotFoundExceptions() {
        // Arrange
        when(foodRepository.getAvailableServings(anyLong())).thenReturn(10);
        when(foodRepository.findById(anyLong())).thenReturn(Optional.of(new Food()));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.addItem(request));
    }

    @Test
    @DisplayName("Цена не найдена")
    void addItem_ThrowsPriceNotFoundExceptions() {
        // Arrange
        when(foodRepository.getAvailableServings(anyLong())).thenReturn(10);
        when(foodRepository.findById(anyLong())).thenReturn(Optional.of(new Food()));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(priceRepository.findByFoodIdAndIsActiveTrue(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PriceNotFoundException.class, () -> cartService.addItem(request));
    }
}
