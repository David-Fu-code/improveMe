package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.CategoryRequest;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Create the category
    public Category createCategory(CategoryRequest request) {

        // Validation
        if (categoryRepository.existsByName(request.getName().toLowerCase())) {
                throw new AuthException("Category name already exists", 401);
        }

        // Create entity
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }

    // Obtain category by id
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AuthException("Category not found", 404));
    }

    // List categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Update category
    public Category updateCategory(Long id, CategoryRequest request) {

        // Validation
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AuthException("category ID not found", 404));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new AuthException("Category name cannot be empty", 403);
        } else if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new AuthException("Category description cannot be empty", 403);
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);

        return category;
    }

    // Eliminate category
    public String deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new AuthException("Category not found", 404);
        }
        categoryRepository.deleteById(id);
        return "Category successfully deleted";
    }
}
