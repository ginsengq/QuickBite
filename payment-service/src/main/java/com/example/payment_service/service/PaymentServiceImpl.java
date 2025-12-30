package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentCompletedEvent;
import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.dto.ProcessPaymentRequest;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.entity.PaymentStatus;
import com.example.payment_service.event.PaymentEventPublisher;
import com.example.payment_service.exception.PaymentNotFoundException;
import com.example.payment_service.mapper.PaymentMapper;
import com.example.payment_service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventPublisher paymentEventPublisher;
    private final RestTemplate restTemplate;
    private final String orderServiceUrl;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            PaymentEventPublisher paymentEventPublisher,
            RestTemplate restTemplate,
            @Value("${order.service.url:http://order-service:8080}") String orderServiceUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentEventPublisher = paymentEventPublisher;
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        log.info("processing payment for order: {}", request.getOrderId());

        // check if payment already exists for this order
        paymentRepository.findByOrderId(request.getOrderId())
                .ifPresent(payment -> {
                    throw new IllegalStateException("Payment already exists for order: " + request.getOrderId());
                });

        // get order details to get the total amount
        Long orderAmount = getOrderAmount(request.getOrderId());

        // create payment entity
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(1L); // in real app, get from JWT token
        payment.setAmount(orderAmount);
        payment.setCurrency("KZT");
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        payment.setTransactionId(UUID.randomUUID().toString());

        // simulate payment processing
        try {
            Thread.sleep(1000); // simulate payment gateway call
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentGatewayResponse("Payment processed successfully");
            log.info("payment processed successfully for order: {}", request.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setPaymentGatewayResponse("Payment processing failed");
            log.error("payment processing failed for order: {}", request.getOrderId());
        }

        Payment savedPayment = paymentRepository.save(payment);

        // publish payment completed event
        if (savedPayment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            PaymentCompletedEvent event = new PaymentCompletedEvent();
            event.setPaymentId(savedPayment.getId());
            event.setOrderId(savedPayment.getOrderId());
            event.setUserId(savedPayment.getUserId());
            event.setAmount(savedPayment.getAmount());
            event.setPaymentStatus(savedPayment.getPaymentStatus().name());
            event.setTransactionId(savedPayment.getTransactionId());
            event.setCompletedAt(savedPayment.getUpdatedAt());
            paymentEventPublisher.publishPaymentCompleted(event);
        }

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        log.info("getting payment by id: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        log.info("getting payment by order id: {}", orderId);
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order: " + orderId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        log.info("getting payments for user: {}", userId);
        return paymentRepository.findByUserId(userId).stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        log.info("getting all payments");
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Long getOrderAmount(Long orderId) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId;
            Map<String, Object> order = restTemplate.getForObject(url, Map.class);
            if (order != null && order.containsKey("totalPrice")) {
                Object totalPrice = order.get("totalPrice");
                if (totalPrice instanceof Number) {
                    return ((Number) totalPrice).longValue();
                }
            }
            log.warn("Could not get order amount, using default 10000");
            return 10000L;
        } catch (Exception e) {
            log.error("Failed to get order amount: {}", e.getMessage());
            return 10000L;
        }
    }
}

