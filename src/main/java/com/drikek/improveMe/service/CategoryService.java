package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.CategoryRequest;
import com.drikek.improveMe.dto.CategoryResponse;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.CategoryRepository;
import com.drikek.improveMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Create the category
    public CategoryResponse createCategory(CategoryRequest request, String userName) {

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AuthException("User not found", 202));

        // Create entity
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);

        return convertToResponse(savedCategory);
    }

    // Obtain category by id
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AuthException("Category not found", 404));

        return convertToResponse(category);
    }

    // List categories
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        // map categories to response
        return categories.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Update category
    public CategoryResponse updateCategory(Long id, CategoryRequest request, String userName) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AuthException("category ID not found", 404));

        // Check if the user is the owner of the category
        if (!category.getUser().getEmail().equals(userName)) {
            throw new AuthException("Unauthorized", 401);
        }

        // Update category
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updateCategory = categoryRepository.save(category);

        return convertToResponse(updateCategory);
    }

    // Eliminate category
    public void deleteCategory(Long id, String userName) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AuthException("category ID not found", 404));

        // Check if the user is the owner of the category
        if (!category.getUser().getEmail().equals(userName)) {
            throw new AuthException("Unauthorized", 401);
        }

        categoryRepository.delete(category);
    }

    // manual mapping
    private CategoryResponse convertToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return response;
    }
}
