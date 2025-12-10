package com.example.order_service.event;
import com.example.order_service.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka") // будет использоваться, если профиль kafka НЕ активен
public class LoggingOrderEventPublisher implements OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingOrderEventPublisher.class);

    @Override
    public void publishOrderCreated(Order order) {
        log.info("[EVENT][STUB] Order created: id={}", order.getId());
    }

    @Override
    public void publishOrderStatusChanged(Order order) {
        log.info("[EVENT][STUB] Order status changed: id={}, status={}",
                order.getId(), order.getStatus());
    }
}
