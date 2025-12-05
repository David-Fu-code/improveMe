package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.CategoryRequest;
import com.drikek.improveMe.dto.CategoryResponse;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.service.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Obtain all Categories
    @GetMapping
    public ResponseEntity<@NonNull List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Obtain Category by ID
    @GetMapping("/{id}")
    public ResponseEntity<@NonNull Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    // Create new Category
    @PostMapping
    public ResponseEntity<@NonNull CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.ok(
                CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build()
        );
    }

    // Update Category
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        Category updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updated);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        String content = categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                Map.of("message", "Category deleted successfully",
                        "deletedId", id)
        );
    }
}
