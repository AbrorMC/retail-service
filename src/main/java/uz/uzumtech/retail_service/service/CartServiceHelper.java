package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.entity.Cart;
import uz.uzumtech.retail_service.entity.OrderItem;

public interface CartServiceHelper {

    OrderItem saveItem(OrderItem item);
    void saveCart(Cart cart);

}
