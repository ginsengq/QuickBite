package com.example.restaurant_service.exception;

public class DuplicateCategoryException extends RuntimeException {
    public DuplicateCategoryException(String name) {
        super("category with name '" + name + "' already exists");
    }
}
