package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.service.CategoryService;
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
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category management APIs")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category", description = "Admin only - creates a new food category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("received request to create category: {}", request.getName());
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Returns category details by ID")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        log.info("received request to get category: {}", id);
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns list of all food categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        log.info("received request to get all categories");
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category", description = "Admin only - deletes a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("received request to delete category: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
