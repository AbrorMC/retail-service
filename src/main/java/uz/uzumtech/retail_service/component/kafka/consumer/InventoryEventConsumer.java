package uz.uzumtech.retail_service.component.kafka.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryEventConsumer {

    OrderService orderService;

    @KafkaListener(topics = "${kafka.topic.inventory-events-topic}", containerFactory = "inventoryEventFactory")
    public void inventoryEventListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        if (payload.message().equals(EventStatus.INVENTORY_RESERVED.toString())) {

        } else {

        }
    }
}
