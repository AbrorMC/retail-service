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
import uz.uzumtech.retail_service.constant.enums.FinancialState;
import uz.uzumtech.retail_service.constant.enums.OrderStatus;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
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
    public void inventoryEventListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        if (payload.message().equals(EventStatus.INVENTORY_RESERVED.toString())) {
            log.info("Inventory reserved successfully for order id: {}", payload.key());

            orderService.updateStatus(Long.parseLong(payload.key()), OrderStatus.COMPLETED);

            reportService.registerIncome(BigDecimal.ONE);
            reportService.registerExpense(BigDecimal.ZERO);
            //TODO: fix payload type to get actual amount for financial records
        } else {
            log.error("Inventory reservation failed for order id: {}", payload.key());

            orderService.updateStatus(Long.parseLong(payload.key()), OrderStatus.CANCELED);

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
