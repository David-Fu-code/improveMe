package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.AssessmentRequest;
import com.drikek.improveMe.dto.AssessmentResponse;
import com.drikek.improveMe.service.AssessmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<@NonNull AssessmentResponse> createAssessment(@AuthenticationPrincipal UserDetails userDetails,
                                                                        @RequestBody AssessmentRequest request) {

        AssessmentResponse response = assessmentService.createAssessment(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<@NonNull List<AssessmentResponse>> getAssessmentForUser(@AuthenticationPrincipal UserDetails userDetails) {

        List<AssessmentResponse> response = assessmentService.getAssessmentsForUser(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{assessmentId}")
    public ResponseEntity<@NonNull AssessmentResponse> getAssessmentById(@PathVariable Long assessmentId) {

        AssessmentResponse response = assessmentService.getAssessmentById(assessmentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/latest")
    public ResponseEntity<@NonNull AssessmentResponse> getLatestAssessment(@AuthenticationPrincipal UserDetails userDetails,
                                                                           @RequestParam Long categoryId) {

        AssessmentResponse response = assessmentService.getLatestAssessment(userDetails.getUsername(), categoryId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{assessmentId}")
    public ResponseEntity<?> deleteAssessment(@PathVariable Long assessmentId) {

        assessmentService.deleteAssessment(assessmentId);

        return ResponseEntity.ok("Assessment deleted successfully");
    }
}
