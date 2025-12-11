package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.entity.Category;
import com.example.restaurant_service.exception.CategoryNotFoundException;
import com.example.restaurant_service.exception.DuplicateCategoryException;
import com.example.restaurant_service.mapper.CategoryMapper;
import com.example.restaurant_service.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, 
                              CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("creating new category: {}", request.getName());
        
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateCategoryException(request.getName());
        }
        
        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        
        log.info("category created successfully with id: {}", saved.getId());
        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("fetching category by id: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        log.info("fetching all categories");
        
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("deleting category with id: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        
        categoryRepository.delete(category);
        
        log.info("category deleted successfully: {}", id);
    }
}
