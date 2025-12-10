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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
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
        // 1. Проверка ресторана
        restaurantClient.validateRestaurantExists(request.getRestaurantId());

        // TODO: через RestaurantClient можно получить реальные цены и посчитать totalPrice

        // 2. Маппинг
        Order order = orderMapper.toEntity(request);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        if (order.getItems() != null) {
            order.getItems().forEach(i -> i.setOrder(order));
        }

        // 3. Сохранение
        Order saved = orderRepository.save(order);

        // 4. Публикация события
        orderEventPublisher.publishOrderCreated(saved);

        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());
        order.setStatus(newStatus);
        order.setUpdatedAt(Instant.now());

        orderEventPublisher.publishOrderStatusChanged(order);

        return orderMapper.toResponse(order);
    }
}