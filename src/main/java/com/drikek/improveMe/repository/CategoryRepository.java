package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Repository
public interface CategoryRepository extends JpaRepository<@NonNull Category, @NonNull Long> {

    // Find category by name
    boolean existsByName(String name);

    // Find categories by part of name (search)
    List<Category> findByNameContainingIgnoreCase(String keyword);

    // Find categories by description
    List<Category> findByDescriptionContainingIgnoreCase(String keyword);

    Optional<Category> findByDescription(String description);
}
