package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.PaymentWebhookDto;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    PageResponse<OrderResponse> getAllOrders(int page, int size);
    void updateStatusOnPaymentSuccess(PaymentWebhookDto webhookData);

}
