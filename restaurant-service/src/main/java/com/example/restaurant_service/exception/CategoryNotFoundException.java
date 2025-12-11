package com.example.restaurant_service.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("category with id " + id + " not found");
    }
    
    public CategoryNotFoundException(String name) {
        super("category with name '" + name + "' not found");
    }
}
