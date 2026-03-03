package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.Order;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.mapper.OrderMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.impl.OrderServiceImpl;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderTransactionService orderTransactionService;
    @Mock
    private CartTransactionService cartTransactionService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Успешное создание заказа")
    void createOrder_Success() {
        // Arrange
        Long cartId = 1L;

        Cart cart = new Cart();
        cart.setId(cartId);

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());
        cart.setItems(items);

        cart.setItemCount(1);
        cart.setTotalAmount(new BigDecimal("100.00"));
        cart.setUserId(10L);

        OrderRequest request = new OrderRequest(cartId);
        Order order = new Order();
        OrderResponse expectedResponse = new OrderResponse(
                1L,
                "1",
                OrderStatus.CREATED,
                1,
                new BigDecimal("100.00"),
                true,
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );

        when(orderMapper.toEntity(request)).thenReturn(order);
        when(cartRepository.findByIdWithItems(cartId)).thenReturn(Optional.of(cart));
        when(orderTransactionService.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(expectedResponse);

        // Act
        OrderResponse actualResponse = orderService.createOrder(request);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(cart.getItemCount()).isZero();
        assertThat(cart.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);

        // Verify
        verify(orderTransactionService).save(order);
        verify(cartRepository).findByIdWithItems(cartId);
        verify(cartTransactionService).saveCart(cart);
    }

    @Test
    @DisplayName("createOrder: Ошибка, если корзина не найдена")
    void createOrder_CartNotFound_ThrowsException() {
        // Arrange
        Long cartId = 1L;
        OrderRequest request = new OrderRequest(cartId);

        when(cartRepository.findByIdWithItems(cartId)).thenReturn(Optional.empty());

        // Assert
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessageContaining(cartId.toString());

        // Verify
        verifyNoInteractions(orderTransactionService);
    }

    @Test
    @DisplayName("updateStatus: Успешное обновление на COMPLETED (деактивация заказа)")
    void updateStatus_ToCompleted_SetsActiveFalse() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setActive(true);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        orderService.updateStatus(orderId, OrderStatus.COMPLETED);

        // Assert
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(order.isActive()).isFalse();

        // Verify
        verify(orderTransactionService).save(order);
    }

    @Test
    @DisplayName("updateStatus: Ошибка, если заказ не найден")
    void updateStatus_OrderNotFound_ThrowsException() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Assert
        assertThatThrownBy(() -> orderService.updateStatus(orderId, OrderStatus.COMPLETED))
                .isInstanceOf(OrderNotFoundException.class);

        // Verify
        verify(orderTransactionService, never()).save(any());
    }

    @Test
    @DisplayName("Получение списка заказов пользователя")
    void getAllOrders_ShouldReturnPageResponse_WhenOrdersExist() {
        // Arrange
        int pageNumber = 1;
        int size = 10;
        Long userId = 1L;

        var pageable = PaginationValidator.validate(pageNumber, size);
        var page = new PageImpl<>(List.of(new Order()));

        var expectedResponse = new PageResponse<>(
                List.of(new OrderResponse(
                        1L,
                        "1",
                        OrderStatus.CREATED,
                        1,
                        new BigDecimal("100.00"),
                        true,
                        LocalDateTime.now().toString(),
                        LocalDateTime.now().toString()
                )), 1, 1, 1, true);

        when(orderRepository.findAllByUserId(userId, pageable)).thenReturn(page);
        when(orderMapper.toPageResponse(page)).thenReturn(expectedResponse);

        // Act
        var actualResponse = orderService.getAllOrders(pageNumber, size, userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify
        verify(orderRepository).findAllByUserId(eq(userId), eq(pageable));
        verify(orderMapper).toPageResponse(page);
    }
}