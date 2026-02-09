package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

import java.util.List;

public interface CartService {

    CartItemResponse addItem(CartItemRequest request);
    List<CartItemResponse> getItemsOfCart(Long cartId);

}
