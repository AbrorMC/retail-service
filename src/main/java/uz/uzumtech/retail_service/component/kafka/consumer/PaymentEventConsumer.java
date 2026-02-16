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

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventConsumer {

    OrderRepository orderRepository;
    InventoryCommandProducer inventoryCommandProducer;

    @KafkaListener(topics = "${kafka.topic.payment-events-topic}", containerFactory = "paymentEventFactory")
    public void paymentEventListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        if (payload.message().equals(EventStatus.PAYMENT_FAILED.toString())) return;

        //TODO: move to updateStatus() method of orderService
        orderRepository.findById(Long.parseLong(payload.correlationId()))
                .ifPresent(order -> {
                    order.setStatus(OrderStatus.PAID);
                    orderRepository.save(order);
                });

        inventoryCommandProducer.sendMessage(
                new KafkaMessageDto(
                    payload.key(),
                    payload.correlationId(),
                    "RESERVE_ITEMS"
                )
        );

        log.info("paymentEventListener consumer {}", payload);
    }
}
