package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.AIPlanRequest;
import com.drikek.improveMe.dto.AIPlanResponse;
import com.drikek.improveMe.entity.AIPlan;
import com.drikek.improveMe.entity.Assessment;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.AIPlanRepository;
import com.drikek.improveMe.repository.AssessmentRepository;
import com.drikek.improveMe.repository.CategoryRepository;
import com.drikek.improveMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AIPlanService {

    private final AIPlanRepository aiPlanRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AssessmentRepository assessmentRepository;

    // Create AI plan
    public AIPlanResponse createAIPlan(String userName, AIPlanRequest request) {

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AuthException("username not found: " + userName, 404));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AuthException("category not found", 404));

        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new AuthException("assessment not found", 404));

        AIPlan aiPlan = new AIPlan();
        aiPlan.setUser(user);
        aiPlan.setCategory(category);
        aiPlan.setAssessment(assessment);
        aiPlan.setTitle(request.getTitle());
        aiPlan.setDescription(request.getDescription());
        aiPlan.setStatus(request.getStatus());
        aiPlan.setStartDate(request.getStartDate());
        aiPlan.setEndDate(request.getEndDate());
        aiPlan.setCreatedAt(LocalDateTime.now());

        AIPlan savedAIPlan = aiPlanRepository.save(aiPlan);

        return toAIPlanResponse(savedAIPlan);

    }

    public List<AIPlanResponse> getAIPlansForUser(String userName) {

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AuthException("Username not found", 404));

        List<AIPlan> aiPlans = aiPlanRepository.findByUserId(user.getId());

        return aiPlans.stream()
                .map(this::toAIPlanResponse)
                .toList();
    }

    public AIPlanResponse getAIPlanById(Long planId) {

        AIPlan aiPlan = aiPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("AIPlan not found"));

        return toAIPlanResponse(aiPlan);
    }

    public AIPlanResponse getLatestAIPlan(Long userId, Long categoryId) {

        Optional<AIPlan> aiPlan = aiPlanRepository.
                findFirstByUserIdAndCategoryIdOrderByCreatedAtDesc(userId, categoryId);

        return aiPlan
                .map(this::toAIPlanResponse)
                .orElseThrow(() -> new EntityNotFoundException("Latest aiPlan not found for userID: " + userId + " and category ID: " + categoryId));

    }

    public void deleteAIPlan(Long planID) {

        AIPlan aiPlan = aiPlanRepository.findById(planID)
                .orElseThrow(() -> new AuthException("AIPlan not found", 404));

        aiPlanRepository.delete(aiPlan);
    }

    @Transactional
    public AIPlanResponse updateAIPlan(Long planID, AIPlanRequest request) {

        AIPlan aiPlan = aiPlanRepository.findById(planID)
                .orElseThrow(() -> new AuthException("AIPlan not found", 404));

        aiPlan.setStatus(request.getStatus());

        if ("completed".equalsIgnoreCase(request.getStatus())) {
            aiPlan.setCompletedAt(LocalDateTime.now());
        }

        AIPlan updateAIPlan = aiPlanRepository.save(aiPlan);

        return toAIPlanResponse(updateAIPlan);
    }

    private AIPlanResponse toAIPlanResponse(AIPlan saveAIPlan) {

        if (saveAIPlan == null) return null;

        return new AIPlanResponse(
                saveAIPlan.getId(),
                saveAIPlan.getUser() != null ? saveAIPlan.getUser().getId() : null,
                saveAIPlan.getCategory() != null ? saveAIPlan.getCategory().getId() : null,
                saveAIPlan.getAssessment() != null ? saveAIPlan.getAssessment().getId() : null,
                saveAIPlan.getCategory() != null ? saveAIPlan.getCategory().getName() : null,
                saveAIPlan.getTitle(),
                saveAIPlan.getDescription(),
                saveAIPlan.getStatus(),
                saveAIPlan.getStartDate(),
                saveAIPlan.getEndDate(),
                saveAIPlan.getCreatedAt(),
                saveAIPlan.getCompletedAt()


        );
    }
}
