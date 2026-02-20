package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryService {

    EventStatus consumeIngredients(Long orderId);
    BigDecimal getIncome();
    BigDecimal getExpense();

}
