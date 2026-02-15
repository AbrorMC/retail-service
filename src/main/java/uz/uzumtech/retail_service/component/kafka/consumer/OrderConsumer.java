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

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderConsumer {

    @KafkaListener(topics = "${kafka.topic.order-topic}", containerFactory = "orderFactory")
    public void orderListener(@Payload OrderDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        log.info("orderListener consumer {}", payload);
    }
}
