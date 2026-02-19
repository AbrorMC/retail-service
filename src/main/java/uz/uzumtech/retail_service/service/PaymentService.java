package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    void updatePaymentStatus(Long Id, PaymentStatus status);
}
