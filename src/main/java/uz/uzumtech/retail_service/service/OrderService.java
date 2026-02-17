package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    PageResponse<OrderResponse> getAllOrders(int page, int size);

    void updateStatus(Long orderId, OrderStatus status);

}
