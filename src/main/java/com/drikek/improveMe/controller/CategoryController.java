package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.CategoryRequest;
import com.drikek.improveMe.dto.CategoryResponse;
import com.drikek.improveMe.service.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<@NonNull List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Obtain Category by ID
    @GetMapping("/{id}")
    public ResponseEntity<@NonNull CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    // Create new Category
    @PostMapping
    public ResponseEntity<@NonNull CategoryResponse> createCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.createCategory(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    // Update Category
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull CategoryResponse> updateCategory(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestBody CategoryRequest request) {
        CategoryResponse updatedResponse = categoryService.updateCategory(id, request, userDetails.getUsername());
        return ResponseEntity.ok(updatedResponse);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        // Pass username to validate owner
        categoryService.deleteCategory(id, userDetails.getUsername());

        return ResponseEntity.ok(
                Map.of("message", "Category deleted successfully",
                        "deletedId", id)
        );
    }
}
