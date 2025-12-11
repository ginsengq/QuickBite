package com.example.restaurant_service.event;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class MenuUpdatedEvent {

    private Long restaurantId;
    private String restaurantName;
    private List<MenuItemUpdate> updatedItems;
    private Instant timestamp;

    @Data
    public static class MenuItemUpdate {
        private Long menuItemId;
        private String name;
        private Long price;
        private Boolean isAvailable;
    }
}
