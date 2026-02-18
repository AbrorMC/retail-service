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
import uz.uzumtech.retail_service.dto.kafka.PaymentEventDto;

@Slf4j
@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventProducer {

    KafkaProps kafkaProps;
    KafkaTemplate<String, PaymentEventDto> paymentEventsTemplate;

    public PaymentEventProducer(KafkaProps kafkaProps,
                                @Qualifier("paymentEventsTopic")
                         KafkaTemplate<String, PaymentEventDto> paymentEventsTemplate) {
        this.kafkaProps = kafkaProps;
        this.paymentEventsTemplate = paymentEventsTemplate;
    }


    public void sendMessage(final PaymentEventDto payload) {
        final Message<PaymentEventDto> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, kafkaProps.getTopic().getPaymentEventsTopic())
                .setHeader(KafkaHeaders.KEY, payload.key())
                .setHeader(KafkaHeaders.CORRELATION_ID, payload.correlationId())
                .build();
        paymentEventsTemplate.send(message);
    }
}
