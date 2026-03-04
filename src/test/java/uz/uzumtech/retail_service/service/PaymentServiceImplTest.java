package uz.uzumtech.retail_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.uzumtech.retail_service.component.adapter.TransactionServiceAdapter;
import uz.uzumtech.retail_service.constant.enums.Currency;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.constant.enums.TransactionType;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.dto.response.PaymentResponse;
import uz.uzumtech.retail_service.entity.Order;
import uz.uzumtech.retail_service.entity.Payment;
import uz.uzumtech.retail_service.exception.PaymentNotFoundException;
import uz.uzumtech.retail_service.mapper.PaymentMapper;
import uz.uzumtech.retail_service.repository.OrderRepository;
import uz.uzumtech.retail_service.repository.PaymentRepository;
import uz.uzumtech.retail_service.service.impl.PaymentServiceImpl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private TransactionServiceAdapter transactionServiceAdapter;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentTransactionService paymentTransactionService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Создание платежа: Успешный сценарий")
    void createPayment_ShouldReturnResponse_WhenOrderIsActive() {
        // Arrange
        Long orderId = 1L;
        var request = new PaymentRequest(
                orderId,
                TransactionType.PAYMENT,
                BigDecimal.valueOf(100.00),
                Currency.UZS,
                "a",
                "b",
                "c",
                "d",
                UUID.randomUUID()
        );

        var order = new Order();
        order.setActive(true);

        var paymentEntity = new Payment();
        var expectedResponse = new PaymentResponse(
                1L,
                orderId,
                PaymentStatus.CREATED,
                request.amount(),
                request.currency(),
                OffsetDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(request)).thenReturn(paymentEntity);
        when(transactionServiceAdapter.sendTransaction(request)).thenReturn(expectedResponse);

        // Act
        var actualResponse = paymentService.createPayment(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify
        verify(paymentTransactionService).save(paymentEntity);
        verify(transactionServiceAdapter).sendTransaction(request);
    }

    @Test
    @DisplayName("Создание платежа: Ошибка, если заказ неактивен")
    void createPayment_ShouldThrowException_WhenOrderIsInactive() {
        // Arrange
        Long orderId = 1L;
        var request = new PaymentRequest(
                orderId,
                TransactionType.PAYMENT,
                BigDecimal.valueOf(100.00),
                Currency.UZS,
                "a",
                "b",
                "c",
                "d",
                UUID.randomUUID()
        );

        var order = new Order();
        order.setActive(false);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(request));

        // Verify
        verifyNoInteractions(paymentMapper);
        verifyNoInteractions(paymentTransactionService);
        verifyNoInteractions(transactionServiceAdapter);
    }

    @Test
    @DisplayName("Обновление статуса: Успешный сценарий")
    void updatePaymentStatus_ShouldSavePayment_WhenPaymentExists() {
        // Arrange
        Long referenceId = 100L;
        PaymentStatus newStatus = PaymentStatus.COMPLETED;
        var payment = new Payment();
        payment.setReferenceId(referenceId);

        when(paymentRepository.findByReferenceId(referenceId)).thenReturn(Optional.of(payment));

        // Act
        paymentService.updatePaymentStatus(referenceId, newStatus);

        // Assert
        assertEquals(newStatus, payment.getStatus());

        // Verify
        verify(paymentTransactionService).save(payment);
    }

    @Test
    @DisplayName("Обновление статуса: Ошибка, если платеж не найден")
    void updatePaymentStatus_ShouldThrowException_WhenPaymentNotFound() {
        // Arrange
        Long referenceId = 100L;
        when(paymentRepository.findByReferenceId(referenceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.updatePaymentStatus(referenceId, PaymentStatus.FAILED));

        // Verify
        verify(paymentTransactionService, never()).save(any());
    }
}