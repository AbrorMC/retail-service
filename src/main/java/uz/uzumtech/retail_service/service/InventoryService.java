package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.entity.OrderItem;

import java.util.List;

public interface InventoryService {

    boolean reserveItems(Long orderId);

}
