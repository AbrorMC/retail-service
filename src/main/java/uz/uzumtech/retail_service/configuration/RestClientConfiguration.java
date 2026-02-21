package uz.uzumtech.retail_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import uz.uzumtech.retail_service.handler.RestClientExceptionHandler;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient restClient(RestClient.Builder builder,
                                 @Value("${services.transaction-processing.url}") String baseUrl) {
        return builder
                .requestFactory(clientHttpRequestFactory())
                .defaultStatusHandler(new RestClientExceptionHandler())
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        var settings = HttpClientSettings
                .defaults()
                .withReadTimeout(Duration.ofSeconds(10))
                .withConnectTimeout(Duration.ofSeconds(5));

        return ClientHttpRequestFactoryBuilder.jdk().build(settings);
    }
}
