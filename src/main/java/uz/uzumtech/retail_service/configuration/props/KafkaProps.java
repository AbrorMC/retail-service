package uz.uzumtech.retail_service.configuration.props;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "kafka")
public class KafkaProps {

    Topic topic;

    String bootstrapServers;
    String clientId;
    String clientDnsLookup;
    String acksConfig;
    String retriesConfig;
    int batchSizeConfig;
    long lingerMsConfig;
    int bufferMemoryConfig;
    String saslProtocol;
    String groupId;
    String autoOffsetResetConfig;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Topic {

        String paymentEventsTopic;
    }

}
