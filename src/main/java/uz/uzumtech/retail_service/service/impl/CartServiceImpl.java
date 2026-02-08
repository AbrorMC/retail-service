package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.mapper.CartItemMapper;
import uz.uzumtech.retail_service.repository.CartItemRepository;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.service.CartService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartItemMapper cartItemMapper;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    FoodRepository foodRepository;

    @Override
    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        var cart = cartRepository
                .findById(request.cartId())
                .orElseThrow();

        var food = foodRepository
                .findById(request.foodId())
                .orElseThrow();

        var cartItem = cartItemMapper.toEntity(request);
        cartItem.setFood(food);

        cart.addItem(cartItem);

        return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
    }
}
