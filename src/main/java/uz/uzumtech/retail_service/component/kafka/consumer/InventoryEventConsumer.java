package uz.uzumtech.retail_service.component.kafka.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.component.kafka.producer.PaymentCommandProducer;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.dto.kafka.InventoryEventDto;
import uz.uzumtech.retail_service.service.OrderService;
import uz.uzumtech.retail_service.service.ReportService;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryEventConsumer {

    OrderService orderService;
    PaymentCommandProducer paymentCommandProducer;
    ReportService reportService;

    @KafkaListener(topics = "${kafka.topic.inventory-events-topic}", containerFactory = "inventoryEventFactory")
    public void inventoryEventListener(@Payload InventoryEventDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        if (payload.message().status().equals(EventStatus.INVENTORY_RESERVED)) {
            log.info("Inventory reserved successfully for order id: {}", payload.key());

            orderService.updateStatus(Long.parseLong(payload.key()), OrderStatus.COMPLETED);

            reportService.registerIncome(payload.message().income());
            reportService.registerExpense(payload.message().expense());
        } else {
            log.error("Inventory reservation failed for order id: {}", payload.key());

            orderService.updateStatus(Long.parseLong(payload.key()), OrderStatus.CANCELLED);

            paymentCommandProducer.sendMessage(
                    new KafkaMessageDto(
                            payload.key(),
                            EventStatus.REFUND_PAYMENT.toString(),
                            payload.correlationId()
                    )
            );
        }
    }
}
