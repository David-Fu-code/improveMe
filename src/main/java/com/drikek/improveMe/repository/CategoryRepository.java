package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // findbyName throws error, this version is much cleaner
    boolean existsByName(String name);

    // Find categories by part of name (search)
    List<Category> findByNameContainingIgnoreCase(String keyword);

    // Find categories by description
    List<Category> findByDescriptionContainingIgnoreCase(String keyword);

    Optional<Category> findByDescription(String description);
}
