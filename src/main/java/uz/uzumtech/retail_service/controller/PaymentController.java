package uz.uzumtech.retail_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;
import uz.uzumtech.retail_service.service.PaymentService;

@Slf4j
@RestController
@RequestMapping("api/core/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Object webhookData) {
        log.info("Received webhook from Transactions Service: {}", webhookData);
        return ResponseEntity.ok().build();
    }
}
