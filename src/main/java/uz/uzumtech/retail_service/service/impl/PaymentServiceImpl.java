package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.component.adapter.TransactionServiceAdapter;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;
import uz.uzumtech.retail_service.entity.Payment;
import uz.uzumtech.retail_service.exception.PaymentNotFoundException;
import uz.uzumtech.retail_service.mapper.PaymentMapper;
import uz.uzumtech.retail_service.repository.PaymentRepository;
import uz.uzumtech.retail_service.service.PaymentService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    TransactionServiceAdapter transactionServiceAdapter;
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        var payment = paymentMapper.toEntity(paymentRequest);
        savePayment(payment);
        return transactionServiceAdapter.sendTransaction(paymentRequest);

    }

    @Override
    @Transactional
    public void updatePaymentStatus(Long id, PaymentStatus status) {
        var payment = paymentRepository
                .findByReferenceId(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with reference id: " + id));

        payment.setStatus(status);
    }

    @Transactional
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
