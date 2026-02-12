package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.PaymentWebhookDto;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.mapper.OrderMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.OrderService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

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

    @Override
    public PageResponse<OrderResponse> getAllOrders(int page, int size) {
        var pageable = PaginationValidator.validate(page, size);

        var orderPage = orderRepository.findAll(pageable);

        return orderMapper.toPageResponse(orderPage);
    }

    @Override
    @Transactional
    public void updateStatusOnPaymentSuccess(PaymentWebhookDto webhookData) {
        var order = orderRepository
                .findById(webhookData.referenceId())
                .orElseThrow(() -> new OrderNotFoundException(webhookData.referenceId().toString()));

        if (webhookData.status() == PaymentStatus.COMPLETED) {
            order.setStatus(OrderStatus.PAID);
        }
    }
}
