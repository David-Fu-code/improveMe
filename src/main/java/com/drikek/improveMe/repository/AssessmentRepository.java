package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Assessment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface AssessmentRepository extends JpaRepository<@NonNull Assessment, @NonNull Long> {

    List<Assessment> findByUserId(Long userId);

    List<Assessment> findByUserIdAndCategoryId(Long userId, Long categoryId);

    Optional<Assessment> findFirstByUserIdAndCategoryIdOrderByCreatedAtDesc(Long userId, Long categoryId);

}
