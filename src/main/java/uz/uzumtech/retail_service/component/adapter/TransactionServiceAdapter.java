package uz.uzumtech.retail_service.component.adapter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceAdapter {

    RestClient restClient;

     public PaymentResponse sendTransaction(PaymentRequest request) {

         var result = restClient
                 .post()
                 .uri("http://localhost:8082/api/v1/transactions")
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(request)
                 .retrieve()
                 .onStatus(HttpStatusCode::isError, (req, res) -> {
                     log.error("Transaction Service returned: {}", res.getStatusCode());
                 })
                 .body(PaymentResponse.class);

         log.info("{}", result);

         return result;
    }

    public PaymentResponse refund(PaymentRequest request) {

        var result = restClient
                .post()
                .uri("http://localhost:8082/api/v1/refunds")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Transaction Service returned: {}", res.getStatusCode());
                })
                .body(PaymentResponse.class);

        log.info("{}", result);

        return result;
    }
}
