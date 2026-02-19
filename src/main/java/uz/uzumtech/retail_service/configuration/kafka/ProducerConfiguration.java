package uz.uzumtech.retail_service.configuration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import uz.uzumtech.retail_service.configuration.props.KafkaProps;
import uz.uzumtech.retail_service.dto.DlqDto;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.dto.kafka.PaymentEventDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProducerConfiguration {

    KafkaProps kafkaProps;

    public Map<String, Object> objectDeserializerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        props.put(ProducerConfig.CLIENT_ID_CONFIG, (kafkaProps.getClientId() + UUID.randomUUID()));
        props.put(ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, kafkaProps.getClientDnsLookup());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProps.getAcksConfig());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProps.getRetriesConfig());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProps.getBatchSizeConfig());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProps.getLingerMsConfig());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProps.getBufferMemoryConfig());


        return props;
    }

    @Bean("dlqTopic")
    public KafkaTemplate<String, DlqDto> dlqTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(objectDeserializerConfigs()));
    }

    @Bean("paymentEventsTopic")
    public KafkaTemplate<String, PaymentEventDto> paymentEventsTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(objectDeserializerConfigs()));
    }

    @Bean("paymentCommandsTopic")
    public KafkaTemplate<String, KafkaMessageDto> paymentCommandsTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(objectDeserializerConfigs()));
    }

    @Bean("inventoryCommandsTopic")
    public KafkaTemplate<String, KafkaMessageDto> inventoryCommandsTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(objectDeserializerConfigs()));
    }

    @Bean("inventoryEventsTopic")
    public KafkaTemplate<String, KafkaMessageDto> inventoryEventsTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(objectDeserializerConfigs()));
    }
}
