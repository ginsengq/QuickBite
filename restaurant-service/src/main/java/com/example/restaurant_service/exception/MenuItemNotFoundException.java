package com.example.restaurant_service.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long id) {
        super("menu item with id " + id + " not found");
    }
}
