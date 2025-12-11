package com.example.order_service.controller;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UpdateOrderStatusRequest;
import com.example.order_service.service.OrderService;
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
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new order", description = "Creates a new order for authenticated user")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("received request to create order for user: {}", request.getUserId());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get order by ID", description = "Returns order details by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        log.info("received request to get order: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get orders by user", description = "Returns all orders for a specific user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@RequestParam Long userId) {
        log.info("received request to get orders for user: {}", userId);
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Admin only - updates order status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        log.info("received request to update order status: {} to {}", id, request.getStatus());
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }
}