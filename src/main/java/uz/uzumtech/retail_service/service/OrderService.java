package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

}
