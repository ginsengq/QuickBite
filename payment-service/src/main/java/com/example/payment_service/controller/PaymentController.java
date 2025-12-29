package com.example.payment_service.controller;

import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.dto.ProcessPaymentRequest;
import com.example.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "Payment management APIs")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Process payment", description = "Processes payment for an order")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        log.info("received request to process payment for order: {}", request.getOrderId());
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get payment by ID", description = "Returns payment details by ID")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        log.info("received request to get payment: {}", id);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get payment by order ID", description = "Returns payment details for an order")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("received request to get payment for order: {}", orderId);
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get payments by user", description = "Returns all payments for a specific user")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUser(@PathVariable Long userId) {
        log.info("received request to get payments for user: {}", userId);
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments", description = "Admin only - returns all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        log.info("received request to get all payments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}

