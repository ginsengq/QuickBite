package com.example.order_service.service;

import com.example.order_service.client.RestaurantClient;
import com.example.order_service.dto.CreateOrderItemRequest;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UpdateOrderStatusRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.event.OrderEventPublisher;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private RestaurantClient restaurantClient;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private OrderResponse testOrderResponse;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUserId(100L);
        testOrder.setRestaurantId(200L);
        testOrder.setStatus(OrderStatus.CREATED);
        testOrder.setTotalPrice(5000L);

        testOrderResponse = new OrderResponse();
        testOrderResponse.setId(1L);
        testOrderResponse.setUserId(100L);
        testOrderResponse.setRestaurantId(200L);
        testOrderResponse.setStatus("CREATED");
        testOrderResponse.setTotalPrice(5000L);

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(100L);
        createOrderRequest.setRestaurantId(200L);
        
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setMenuItemId(300L);
        itemRequest.setQuantity(2);
        createOrderRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    void createOrder_Success() {
        // Given
        doNothing().when(restaurantClient).validateRestaurantExists(anyLong());
        when(orderMapper.toEntity(any(CreateOrderRequest.class)))
                .thenReturn(testOrder);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(testOrder);
        when(orderMapper.toResponse(any(Order.class)))
                .thenReturn(testOrderResponse);
        doNothing().when(orderEventPublisher).publishOrderCreated(any(Order.class));

        // When
        OrderResponse result = orderService.createOrder(createOrderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        // Given
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder));
        when(orderMapper.toResponse(testOrder))
                .thenReturn(testOrderResponse);

        // When
        OrderResponse result = orderService.getOrderById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_NotFound() {
        // Given
        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getOrdersByUser_Success() {
        // Given
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(100L);

        when(orderRepository.findByUserId(100L))
                .thenReturn(Arrays.asList(testOrder, order2));
        when(orderMapper.toResponse(any(Order.class)))
                .thenReturn(testOrderResponse);

        // When
        List<OrderResponse> results = orderService.getOrdersByUser(100L);

        // Then
        assertThat(results).hasSize(2);
        verify(orderRepository).findByUserId(100L);
    }

    @Test
    void updateStatus_Success() {
        // Given
        UpdateOrderStatusRequest updateRequest = new UpdateOrderStatusRequest();
        updateRequest.setStatus("COOKING");
        
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder));
        when(orderMapper.toResponse(any(Order.class)))
                .thenReturn(testOrderResponse);
        doNothing().when(orderEventPublisher).publishOrderStatusChanged(any(Order.class));

        // When
        OrderResponse result = orderService.updateStatus(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(orderEventPublisher).publishOrderStatusChanged(any(Order.class));
    }
}
