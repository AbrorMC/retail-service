package uz.uzumtech.retail_service.component.kafka.consumer;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.component.kafka.producer.InventoryCommandProducer;
import uz.uzumtech.retail_service.component.kafka.producer.InventoryEventProducer;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.repository.OrderItemRepository;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.InventoryService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryCommandConsumer {

    InventoryService inventoryService;
    InventoryEventProducer inventoryEventProducer;

    @KafkaListener(topics = "${kafka.topic.inventory-commands-topic}", containerFactory = "inventoryCommandFactory")
    public void paymentEventListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        var eventStatus = inventoryService.reserveItems(Long.parseLong(payload.correlationId())) ?
                EventStatus.RESERVE_INVENTORY : EventStatus.OUT_OF_STOCK;


//        log.info("paymentEventListener consumer {}", payload);
    }
}
