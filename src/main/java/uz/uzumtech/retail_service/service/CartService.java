package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    CartItemResponse addItem(CartItemRequest request);
    CartResponse getItemsOfCart(Long cartId);

}
