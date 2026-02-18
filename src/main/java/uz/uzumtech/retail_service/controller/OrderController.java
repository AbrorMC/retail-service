package uz.uzumtech.retail_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzumtech.retail_service.component.kafka.producer.PaymentEventProducer;
import uz.uzumtech.retail_service.constant.enums.EventStatus;
import uz.uzumtech.retail_service.dto.PaymentWebhookDto;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.dto.kafka.PaymentEventDto;
import uz.uzumtech.retail_service.dto.request.OrderRequest;
import uz.uzumtech.retail_service.dto.response.OrderResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.service.OrderService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/core/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;
    PaymentEventProducer paymentEventProducer;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> updateStatusOnPaymentSuccess(@RequestBody PaymentWebhookDto webhookData) {
        log.info("Received webhook from Transactions Service: {}", webhookData);

        String status = webhookData.status().toString().equals("COMPLETED") ?
                EventStatus.PAYMENT_SUCCESS.toString() :
                EventStatus.PAYMENT_FAILED.toString();

        PaymentEventDto event = new PaymentEventDto(
                webhookData.referenceId().toString(),
                UUID.randomUUID().toString(),
                webhookData
        );

        paymentEventProducer.sendMessage(event);

        return ResponseEntity.ok().build();
    }
}
