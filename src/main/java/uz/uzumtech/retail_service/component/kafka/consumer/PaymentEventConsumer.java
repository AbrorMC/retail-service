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
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.dto.kafka.PaymentEventDto;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.service.OrderService;
import uz.uzumtech.retail_service.service.PaymentService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventConsumer {

    OrderService orderService;
    InventoryCommandProducer inventoryCommandProducer;
    PaymentService paymentService;

    @KafkaListener(topics = "${kafka.topic.payment-events-topic}", containerFactory = "paymentEventFactory")
    public void paymentEventListener(@Payload PaymentEventDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        if (payload.message().status() == PaymentStatus.REFUND) {
            paymentService.updatePaymentStatus(Long.parseLong(payload.message().referenceId().toString()), PaymentStatus.REFUND);
        }

        var orderStatus = payload.message().status() == PaymentStatus.COMPLETED ?
                OrderStatus.PAID : OrderStatus.CANCELLED;

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
