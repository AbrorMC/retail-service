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
public class PaymentCommandProducer {

    KafkaProps kafkaProps;
    KafkaTemplate<String, KafkaMessageDto> paymentCommandsTemplate;

    public PaymentCommandProducer(KafkaProps kafkaProps,
                                  @Qualifier("paymentCommandsTopic")
                         KafkaTemplate<String, KafkaMessageDto> paymentCommandsTemplate) {
        this.kafkaProps = kafkaProps;
        this.paymentCommandsTemplate = paymentCommandsTemplate;
    }


    public void sendMessage(final KafkaMessageDto payload) {
        final Message<KafkaMessageDto> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, kafkaProps.getTopic().getPaymentEventsTopic())
                .setHeader(KafkaHeaders.KEY, payload.key())
                .setHeader(KafkaHeaders.CORRELATION_ID, payload.correlationId())
                .build();
        paymentCommandsTemplate.send(message);
    }
}
