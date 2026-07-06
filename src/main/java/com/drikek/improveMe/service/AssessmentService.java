package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.AssessmentRequest;
import com.drikek.improveMe.dto.AssessmentResponse;
import com.drikek.improveMe.entity.Assessment;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.AssessmentRepository;
import com.drikek.improveMe.repository.CategoryRepository;
import com.drikek.improveMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // Create assessment
    public AssessmentResponse createAssessment(String userName, AssessmentRequest request) {

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AuthException("UserName not found:" + userName, 404));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AuthException("Category not found", 404));

        Assessment assessment = new Assessment();
        assessment.setUser(user);
        assessment.setCategory(category);
        assessment.setQuestions(request.getQuestions());
        assessment.setAnswers(request.getAnswers());
        assessment.setScore(request.getScore());
        assessment.setLevel(request.getLevel());
        assessment.setCreatedAt(LocalDateTime.now());

        Assessment saveAssessment = assessmentRepository.save(assessment);

        return toAssessmentResponse(saveAssessment);

    }

    // Obtain user assessments
    public List<AssessmentResponse> getAssessmentsForUser(String userName) {

        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AuthException("Username not found: " + userName, 404));

        List<Assessment> assessments = assessmentRepository.findByUserId(user.getId());

        return assessments.stream()
                .map(this::toAssessmentResponse)
                .toList();
    }

    // Obtain assessment
    public AssessmentResponse getAssessmentById(Long assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment with ID: " + assessmentId + "not found"));

        return toAssessmentResponse(assessment);
    }

    // Obtain latest assessment
    public AssessmentResponse getLatestAssessment(Long userId, Long categoryId) {

        Optional<Assessment> latestAssessment = assessmentRepository
                .findFirstByUserIdAndCategoryIdOrderByCreatedAtDesc(userId, categoryId);

        return latestAssessment
                .map(this::toAssessmentResponse)
                .orElseThrow(() -> new EntityNotFoundException("Latest assessment not found for user ID: " + userId + " and category ID: " + categoryId));
    }

    // Delete assessment
    public void deleteAssessment(Long assessmentId){

        assessmentRepository.findById(assessmentId)
                        .orElseThrow(() -> new AuthException("Assessment not found", 404));

        assessmentRepository.deleteById(assessmentId);
    }


    private AssessmentResponse toAssessmentResponse(Assessment saveAssessment) {

        if (saveAssessment == null) return null;

        return new AssessmentResponse(
                saveAssessment.getId(),
                saveAssessment.getUser() != null ? saveAssessment.getUser().getId() : null,
                saveAssessment.getCategory() != null ? saveAssessment.getCategory().getId() : null,
                saveAssessment.getCategory() != null ? saveAssessment.getCategory().getName() : null,
                saveAssessment.getQuestions(),
                saveAssessment.getAnswers(),
                saveAssessment.getScore(),
                saveAssessment.getLevel(),
                saveAssessment.getCreatedAt()
        );

    }
}