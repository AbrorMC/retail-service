package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.mapper.OrderMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderMapper orderMapper;
    OrderRepository orderRepository;
    CartRepository cartRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        var order = orderMapper.toEntity(request);
        var cart = cartRepository
                .findByIdWithItems(request.cartId())
                .orElseThrow(() -> new CartNotFoundException(request.cartId().toString()));

        List<OrderItem> items = cart.getItems();

        order.addAll(items);
        order.setStatus(OrderStatus.CREATED);
        order.setItemCount(cart.getItemCount());
        order.setTotalPrice(cart.getTotalAmount());
        order.setUserId(cart.getUserId());

        cart.setItemCount(0);
        cart.setTotalAmount(BigDecimal.ZERO);

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
