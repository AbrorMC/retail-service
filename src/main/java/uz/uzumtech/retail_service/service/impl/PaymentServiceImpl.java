package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.uzumtech.retail_service.component.adapter.TransactionServiceAdapter;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;
import uz.uzumtech.retail_service.service.PaymentService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    TransactionServiceAdapter transactionServiceAdapter;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        return transactionServiceAdapter.sendTransaction(paymentRequest);

    }
}
