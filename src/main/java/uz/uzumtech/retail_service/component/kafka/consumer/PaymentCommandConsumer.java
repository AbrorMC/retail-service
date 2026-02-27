package uz.uzumtech.retail_service.component.kafka.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uz.uzumtech.retail_service.component.adapter.TransactionServiceAdapter;
import uz.uzumtech.retail_service.dto.KafkaMessageDto;
import uz.uzumtech.retail_service.mapper.PaymentMapper;
import uz.uzumtech.retail_service.repository.PaymentRepository;
import uz.uzumtech.retail_service.service.PaymentService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCommandConsumer {

    TransactionServiceAdapter transactionServiceAdapter;
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    PaymentService paymentService;

    @KafkaListener(topics = "${kafka.topic.payment-commands-topic}", containerFactory = "paymentCommandFactory")
    public void inventoryCommandListener(@Payload KafkaMessageDto payload, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();

        Long id = Long.parseLong(payload.key());
        var paymentEntity = paymentRepository
                .findByReferenceId(id)
                .orElseThrow(() -> new RuntimeException("Payment not found for reference id: " + id));

        var response = transactionServiceAdapter.refund(paymentMapper.toRequest(paymentEntity));

        paymentService.updatePaymentStatus(paymentEntity.getId(), response.status());

        log.info("inventoryCommandListener consumer {}", payload);
    }
}
