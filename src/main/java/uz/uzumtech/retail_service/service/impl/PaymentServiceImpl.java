package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.uzumtech.retail_service.component.adapter.TransactionServiceAdapter;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;
import uz.uzumtech.retail_service.exception.OrderNotFoundException;
import uz.uzumtech.retail_service.exception.PaymentNotFoundException;
import uz.uzumtech.retail_service.mapper.PaymentMapper;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.repository.PaymentRepository;
import uz.uzumtech.retail_service.service.PaymentService;
import uz.uzumtech.retail_service.service.PaymentTransactionService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    TransactionServiceAdapter transactionServiceAdapter;
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    PaymentTransactionService paymentTransactionService;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        var order = orderRepository
                .findById(paymentRequest.referenceId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + paymentRequest.referenceId()));

        if (!order.isActive()) {
            throw new IllegalArgumentException("Cannot create payment for an inactive order with id: " + paymentRequest.referenceId());
        }

        paymentTransactionService.save(paymentMapper.toEntity(paymentRequest));

        return transactionServiceAdapter.sendTransaction(paymentRequest);

    }

    @Override
    public void updatePaymentStatus(Long id, PaymentStatus status) {
        var payment = paymentRepository
                .findByReferenceId(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with reference id: " + id));

        payment.setStatus(status);

        paymentTransactionService.save(payment);
    }
}
