package com.example.order_service.mapper;

import com.example.order_service.*;

import com.example.order_service.dto.CreateOrderItemRequest;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemResponse;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toEntity(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setTotalPrice(0L); // потом посчитаешь по меню

        if (request.getItems() != null) {
            List<OrderItem> items = request.getItems().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());
            order.setItems(items);
        }

        return order;
    }

    private OrderItem toEntity(CreateOrderItemRequest req) {
        OrderItem item = new OrderItem();
        item.setMenuItemId(req.getMenuItemId());
        item.setQuantity(req.getQuantity());
        item.setPrice(0L); // потом возьмешь из Restaurant-сервиса
        return item;
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setUserId(order.getUserId());
        resp.setRestaurantId(order.getRestaurantId());
        resp.setStatus(order.getStatus().name());
        resp.setTotalPrice(order.getTotalPrice());
        resp.setCreatedAt(order.getCreatedAt());
        resp.setUpdatedAt(order.getUpdatedAt());

        if (order.getItems() != null) {
            List<OrderItemResponse> items = order.getItems().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            resp.setItems(items);
        }

        return resp;
    }

    private OrderItemResponse toResponse(OrderItem item) {
        OrderItemResponse resp = new OrderItemResponse();
        resp.setMenuItemId(item.getMenuItemId());
        resp.setQuantity(item.getQuantity());
        resp.setPrice(item.getPrice());
        return resp;
    }
}