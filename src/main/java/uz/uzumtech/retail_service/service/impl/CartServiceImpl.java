package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.CartItem;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.mapper.CartItemMapper;
import uz.uzumtech.retail_service.repository.CartItemRepository;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.service.CartService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.util.List;

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

    @Override
    public List<CartItemResponse> getItemsOfCart(Long cartId) {
        var cart = cartRepository
                .findByIdWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        List<CartItem> items = cart.getItems();

        return items.stream().map(cartItemMapper::toResponse).toList();
    }
}
