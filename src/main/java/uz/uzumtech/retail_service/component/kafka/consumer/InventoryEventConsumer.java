package uz.uzumtech.retail_service.component.kafka.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.internals.events.CompletableEvent;
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
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;

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

            //TODO: Обновление materialized view доступности блюд на уровне БД
//            CompletableFuture.runAsync(() ->
//                    reportService.registerFailedOrder(Long.parseLong(payload.key()), payload.message().income()));

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
