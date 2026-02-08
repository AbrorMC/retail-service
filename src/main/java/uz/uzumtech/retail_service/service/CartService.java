package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;

public interface CartService {

    CartItemResponse addItem(CartItemRequest request);

}
