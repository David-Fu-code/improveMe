package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.AIPlan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@SuppressWarnings("unused")
public interface AIPlanRepository extends JpaRepository<@NonNull AIPlan, @NonNull Long> {

    List<AIPlan> findByUserId(Long userId);

    List<AIPlan> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<AIPlan> findByUserIdAndStatus(Long userId, String status);

    List<AIPlan> findByAssessmentId(Long assessmentId);

    Optional<AIPlan> findFirstByUserIdAndCategoryIdOrderByCreatedAtDesc(Long userId, Long categoryId);

}
