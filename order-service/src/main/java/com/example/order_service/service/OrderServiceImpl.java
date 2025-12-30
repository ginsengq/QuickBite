package com.example.order_service.service;

import com.example.order_service.client.RestaurantClient;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UpdateOrderStatusRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.event.OrderEventPublisher;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantClient restaurantClient;
    private final OrderEventPublisher orderEventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderMapper orderMapper,
                            RestaurantClient restaurantClient,
                            OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.restaurantClient = restaurantClient;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("creating new order for user: {}, restaurant: {}", request.getUserId(), request.getRestaurantId());
        
        // validate restaurant exists
        restaurantClient.validateRestaurantExists(request.getRestaurantId());
        
        // map request to entity
        Order order = orderMapper.toEntity(request);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        if (order.getItems() != null) {
            order.getItems().forEach(i -> i.setOrder(order));
            
            // get menu item prices from restaurant service
            List<Long> menuItemIds = order.getItems().stream()
                    .map(item -> item.getMenuItemId())
                    .toList();
            
            Map<Long, Long> prices = restaurantClient.getMenuItemsPrices(menuItemIds);
            
            // set prices and calculate total
            long totalPrice = 0;
            for (var item : order.getItems()) {
                Long price = prices.getOrDefault(item.getMenuItemId(), 0L);
                item.setPrice(price);
                totalPrice += price * item.getQuantity();
            }
            order.setTotalPrice(totalPrice);
        } else {
            order.setTotalPrice(0L);
        }

        // save order
        Order saved = orderRepository.save(order);
        log.info("order created successfully with id: {}, total: {}", saved.getId(), saved.getTotalPrice());

        // publish order created event
        orderEventPublisher.publishOrderCreated(saved);

        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("fetching order by id: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        log.info("fetching orders for user: {}", userId);
        
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        log.info("updating order status: {} to {}", id, request.getStatus());
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
            order.setStatus(newStatus);
            order.setUpdatedAt(Instant.now());

            orderEventPublisher.publishOrderStatusChanged(order);
            
            log.info("order status updated successfully: {}", id);
            return orderMapper.toResponse(order);
        } catch (IllegalArgumentException e) {
            log.error("invalid order status: {}", request.getStatus());
            throw new IllegalArgumentException("invalid order status: " + request.getStatus());
        }
    }
}