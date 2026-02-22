package uz.uzumtech.retail_service.component.kafka.consumer;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.component.kafka.producer.InventoryEventProducer;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.dto.kafka.InventoryEventDto;
import uz.uzumtech.retail_service.service.InventoryService;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryCommandConsumer {

    InventoryService inventoryService;
    InventoryEventProducer inventoryEventProducer;

    @KafkaListener(topics = "${kafka.topic.inventory-commands-topic}", containerFactory = "inventoryCommandFactory")
    public void inventoryCommandListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        Long orderId = Long.parseLong(payload.key());
        BigDecimal totalCost = inventoryService.consumeIngredients(orderId);

        var status = totalCost.compareTo(BigDecimal.ZERO) > 0 ?
                EventStatus.INVENTORY_RESERVED : EventStatus.OUT_OF_STOCK;

        inventoryEventProducer.sendMessage(
                new InventoryEventDto(
                        payload.key(),
                        payload.correlationId(),
                        new InventoryEventDto.InventoryEventMessage(
                                status,
                                inventoryService.getIncome(orderId),
                                totalCost
                        )
                )
        );

        log.info("inventoryCommandListener consumer {}", payload);
    }
}
