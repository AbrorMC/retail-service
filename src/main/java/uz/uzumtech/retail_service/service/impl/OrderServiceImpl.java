package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.mapper.OrderMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.CartTransactionService;
import uz.uzumtech.retail_service.service.OrderService;
import uz.uzumtech.retail_service.service.OrderTransactionService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderMapper orderMapper;
    OrderRepository orderRepository;
    CartRepository cartRepository;
    OrderTransactionService orderTransactionService;
    CartTransactionService cartTransactionService;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        var cart = cartRepository
                .findByIdWithItems(request.cartId())
                .orElseThrow(() -> new CartNotFoundException(request.cartId().toString()));

        var order = orderMapper.toEntity(request);

        List<OrderItem> orderItems = cart.getItems().stream()
                .<OrderItem>map(cartItem -> OrderItem.builder()
                        .food(cartItem.getFood())
                        .count(cartItem.getCount())
                        .price(cartItem.getPrice())
                        .build())
                .toList();

        order.addAll(orderItems);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(cart.getTotalAmount());
        order.setItemCount(cart.getItemCount());
        order.setUserId(cart.getUserId());
        order.setActive(true);

        cart.removeAllItems();
        cartTransactionService.saveCart(cart);

        return orderMapper.toResponse(orderTransactionService.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getAllOrders(int page, int size, Long userId) {
        var pageable = PaginationValidator.validate(page, size);

        var orderPage = orderRepository.findAllByUserId(userId, pageable);

        return orderMapper.toPageResponse(orderPage);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setStatus(status);

        if (status == OrderStatus.COMPLETED) {
            order.setActive(false);
        }

        orderTransactionService.save(order);
    }
}
