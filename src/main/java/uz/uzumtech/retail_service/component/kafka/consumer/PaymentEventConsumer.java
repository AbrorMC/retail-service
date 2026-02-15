package uz.uzumtech.retail_service.component.kafka.consumer;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.dto.OrderDto;
import uz.uzumtech.retail_service.dto.event.PaymentEventDto;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventConsumer {

    @KafkaListener(topics = "${kafka.topic.payment-events-topic}", containerFactory = "paymentEventFactory")
    public void orderListener(@Payload PaymentEventDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        log.info("paymentEventListener consumer {}", payload);
    }
}
