package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.OrderItem;
import uz.uzumtech.retail_service.repository.CartRepository;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.service.CartTransactionService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartTransactionServiceImpl implements CartTransactionService {

    OrderItemRepository orderItemRepository;
    CartRepository cartRepository;

    @Override
    @Transactional
    public OrderItem saveItem(OrderItem item) {
        return orderItemRepository.save(item);
    }

    @Override
    @Transactional
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
