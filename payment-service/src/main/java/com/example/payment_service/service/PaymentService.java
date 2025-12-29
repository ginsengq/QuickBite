package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.dto.ProcessPaymentRequest;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(ProcessPaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByOrderId(Long orderId);
    List<PaymentResponse> getPaymentsByUserId(Long userId);
    List<PaymentResponse> getAllPayments();
}

