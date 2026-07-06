package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.ImprovementSuggestion;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImprovementSuggestionRepository extends JpaRepository<@NonNull ImprovementSuggestion, @NonNull Long> {

    List<ImprovementSuggestion> findByCategoryId(Long categoryId);
    void deleteById(Long id);
}
