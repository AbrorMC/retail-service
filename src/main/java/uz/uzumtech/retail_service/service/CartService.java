package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.dto.response.CartResponse;

public interface CartService {

    OrderItemResponse addItem(OrderItemRequest request);
    CartResponse getItemsOfCart(Long cartId);

}
