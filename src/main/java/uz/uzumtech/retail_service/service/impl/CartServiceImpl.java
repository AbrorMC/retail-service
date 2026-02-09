package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.CartItem;
import uz.uzumtech.retail_service.entity.Food;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.exception.PriceNotFoundException;
import uz.uzumtech.retail_service.mapper.CartItemMapper;
import uz.uzumtech.retail_service.mapper.CartMapper;
import uz.uzumtech.retail_service.repository.CartItemRepository;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.repository.PriceRepository;
import uz.uzumtech.retail_service.service.CartService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartItemMapper cartItemMapper;
    CartMapper cartMapper;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    FoodRepository foodRepository;
    PriceRepository priceRepository;

    @Override
    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        var cart = cartRepository
                .findById(request.cartId())
                .orElseThrow(() -> new CartNotFoundException(request.cartId().toString()));

        var food = foodRepository
                .findById(request.foodId())
                .orElseThrow(() -> new FoodNotFoundException(request.foodId().toString()));

        var price = priceRepository
                .findByFoodIdAndIsActiveTrue(request.foodId())
                .orElseThrow(() -> new PriceNotFoundException(request.foodId().toString()));

        var cartItem = cartItemMapper.toEntity(request);
        cartItem.setFood(food);
        cartItem.setPrice(price.getPrice());
        cart.addItem(cartItem);

        return cartItemMapper.toResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public CartResponse getItemsOfCart(Long cartId) {
        var cart = cartRepository
                .findByIdWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        return cartMapper.toResponse(cart);
    }
}
