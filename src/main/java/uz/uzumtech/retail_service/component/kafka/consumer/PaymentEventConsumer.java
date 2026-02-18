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
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventConsumer {

    OrderService orderService;
    InventoryCommandProducer inventoryCommandProducer;

    @KafkaListener(topics = "${kafka.topic.payment-events-topic}", containerFactory = "paymentEventFactory")
    public void paymentEventListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        var orderStatus = EventStatus.valueOf(payload.message()) == EventStatus.PAYMENT_SUCCESS ?
                OrderStatus.PAID : OrderStatus.CANCELED;
        orderService.updateStatus(Long.parseLong(payload.key()), orderStatus);

        if (orderStatus == OrderStatus.PAID) {
            inventoryCommandProducer.sendMessage(
                    new KafkaMessageDto(
                            payload.key(),
                            payload.correlationId(),
                            EventStatus.RESERVE_INVENTORY.toString()
                    )
            );
        }
        log.info("paymentEventListener consumer {}", payload);
    }
}
