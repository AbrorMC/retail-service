package uz.uzumtech.retail_service.component.adapter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uz.uzumtech.retail_service.constant.Constants;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceAdapter {

    RestClient restClient;

    @CircuitBreaker(name = "transactionService", fallbackMethod = "fallbackSendTransaction")
     public PaymentResponse sendTransaction(PaymentRequest request) {

         var result = restClient
                 .post()
                 .uri(Constants.TRANSACTIONS)
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(request)
                 .retrieve()
                 .onStatus(HttpStatusCode::isError, (req, res) -> {
                     log.error("Transaction Service returned: {}", res.getStatusCode());
                     throw new RuntimeException("External Service Error: " + res.getStatusCode());
                 })
                 .body(PaymentResponse.class);

         log.info("{}", result);

         return result;
    }

    @CircuitBreaker(name = "transactionService", fallbackMethod = "fallbackRefundTransaction")
    public PaymentResponse refund(PaymentRequest request) {

        var result = restClient
                .post()
                .uri(Constants.REFUNDS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Transaction Service returned: {}", res.getStatusCode());
                    throw new RuntimeException("External Service Error: " + res.getStatusCode());
                })
                .body(PaymentResponse.class);

        log.info("{}", result);

        return result;
    }

    private PaymentResponse fallbackSendTransaction(PaymentRequest request, Throwable t) {
        log.error("Fallback SendTransaction: {}", t.getMessage());
        return new PaymentResponse(
                null,
                request.referenceId(),
                PaymentStatus.FAILED,
                request.amount(),
                request.currency(),
                OffsetDateTime.now()
        );
    }

    // Fallback для возврата
    private PaymentResponse fallbackRefund(PaymentRequest request, Throwable t) {
        log.error("Fallback Refund: {}", t.getMessage());
        return new PaymentResponse(
                null,
                request.referenceId(),
                PaymentStatus.REJECTED,
                request.amount(),
                request.currency(),
                OffsetDateTime.now()
        );
    }
}
