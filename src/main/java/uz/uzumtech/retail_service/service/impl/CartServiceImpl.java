package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.exception.CartNotFoundException;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.exception.OrderItemNotFoundException;
import uz.uzumtech.retail_service.exception.PriceNotFoundException;
import uz.uzumtech.retail_service.mapper.CartMapper;
import uz.uzumtech.retail_service.mapper.OrderItemMapper;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.repository.PriceRepository;
import uz.uzumtech.retail_service.service.CartService;
import uz.uzumtech.retail_service.service.CartTransactionService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    OrderItemMapper orderItemMapper;
    CartMapper cartMapper;
    CartRepository cartRepository;
    OrderItemRepository orderItemRepository;
    FoodRepository foodRepository;
    PriceRepository priceRepository;
    CartTransactionService cartTransactionService;

    @Override
    public OrderItemResponse addItem(OrderItemRequest request) {
        int available = foodRepository.getAvailableServings(request.foodId());

        if (available <= 0) {
            throw new IllegalStateException("Не доступно для выбора блюда с id: " + request.foodId());
        }

        var food = foodRepository
                .findById(request.foodId())
                .orElseThrow(() -> new FoodNotFoundException(request.foodId().toString()));

        var cart = cartRepository
                .findById(request.cartId())
                .orElseThrow(() -> new CartNotFoundException(request.cartId().toString()));

        var price = priceRepository
                .findByFoodIdAndIsActiveTrue(request.foodId())
                .orElseThrow(() -> new PriceNotFoundException(request.foodId().toString()));

        var cartItem = orderItemMapper.toEntity(request);
        cartItem.setFood(food);
        cartItem.setPrice(price.getPrice());
        cart.addItem(cartItem);

        cartTransactionService.saveCart(cart);

        return orderItemMapper.toResponse(cartTransactionService.saveItem(cartItem));
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getItemsOfCart(Long cartId) {
        var cart = cartRepository
                .findByIdWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        return cartMapper.toResponse(cart);
    }

    @Override
    public void deleteItem(Long cartId, Long itemId) {
        var cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        var item = orderItemRepository
                .findById(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(itemId.toString()));

        cart.removeItem(item);

        cartTransactionService.saveCart(cart);
    }

    @Override
    public void clear(Long cartId) {
        var cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        cart.removeAllItems();

        cartTransactionService.saveCart(cart);
    }


}
