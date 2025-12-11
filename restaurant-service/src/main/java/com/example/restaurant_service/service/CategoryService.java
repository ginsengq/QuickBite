package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long id);
}
