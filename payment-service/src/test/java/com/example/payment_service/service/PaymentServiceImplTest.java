package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.dto.ProcessPaymentRequest;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.entity.PaymentStatus;
import com.example.payment_service.event.PaymentEventPublisher;
import com.example.payment_service.exception.PaymentNotFoundException;
import com.example.payment_service.mapper.PaymentMapper;
import com.example.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private PaymentResponse paymentResponse;
    private ProcessPaymentRequest processPaymentRequest;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(100L);
        payment.setUserId(1L);
        payment.setAmount(10000L);
        payment.setCurrency("KZT");
        payment.setPaymentMethod("CARD");
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("txn_123456");

        paymentResponse = new PaymentResponse();
        paymentResponse.setId(1L);
        paymentResponse.setOrderId(100L);
        paymentResponse.setUserId(1L);
        paymentResponse.setAmount(10000L);
        paymentResponse.setCurrency("KZT");
        paymentResponse.setPaymentMethod("CARD");
        paymentResponse.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentResponse.setTransactionId("txn_123456");

        processPaymentRequest = new ProcessPaymentRequest();
        processPaymentRequest.setOrderId(100L);
        processPaymentRequest.setPaymentMethod("CARD");
        processPaymentRequest.setCardNumber("1234567890123456");
        processPaymentRequest.setCardHolderName("John Doe");
        processPaymentRequest.setExpiryDate("12/25");
        processPaymentRequest.setCvv("123");
    }

    @Test
    void processPayment_Success() {
        // Arrange
        when(paymentRepository.findByOrderId(anyLong())).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(paymentResponse);
        doNothing().when(paymentEventPublisher).publishPaymentCompleted(any());

        // Act
        PaymentResponse result = paymentService.processPayment(processPaymentRequest);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        verify(paymentRepository).findByOrderId(anyLong());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void getPaymentById_Success() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(paymentResponse);

        // Act
        PaymentResponse result = paymentService.getPaymentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getOrderId());
        verify(paymentRepository).findById(1L);
    }

    @Test
    void getPaymentById_NotFound() {
        // Arrange
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentNotFoundException.class, () -> {
            paymentService.getPaymentById(999L);
        });
        verify(paymentRepository).findById(999L);
    }

    @Test
    void getPaymentByOrderId_Success() {
        // Arrange
        when(paymentRepository.findByOrderId(100L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(paymentResponse);

        // Act
        PaymentResponse result = paymentService.getPaymentByOrderId(100L);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        verify(paymentRepository).findByOrderId(100L);
    }

    @Test
    void getAllPayments_Success() {
        // Arrange
        List<Payment> payments = Arrays.asList(payment);
        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(paymentResponse);

        // Act
        List<PaymentResponse> result = paymentService.getAllPayments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository).findAll();
    }
}
