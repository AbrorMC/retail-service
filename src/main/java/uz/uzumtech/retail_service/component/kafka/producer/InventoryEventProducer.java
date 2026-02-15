package uz.uzumtech.retail_service.component.kafka.producer;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.configuration.props.KafkaProps;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;

@Slf4j
@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryEventProducer {

    KafkaProps kafkaProps;
    KafkaTemplate<String, KafkaMessageDto> inventoryEventsTemplate;

    public InventoryEventProducer(KafkaProps kafkaProps,
                                  @Qualifier("inventoryCommandsTopic")
                         KafkaTemplate<String, KafkaMessageDto> inventoryEventsTemplate) {
        this.kafkaProps = kafkaProps;
        this.inventoryEventsTemplate = inventoryEventsTemplate;
    }


    public void sendMessage(final KafkaMessageDto payload) {
        final Message<KafkaMessageDto> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, kafkaProps.getTopic().getInventoryEventsTopic())
                .setHeader(KafkaHeaders.KEY, payload.key())
                .setHeader(KafkaHeaders.CORRELATION_ID, payload.correlationId())
                .build();
        inventoryEventsTemplate.send(message);
    }
}
