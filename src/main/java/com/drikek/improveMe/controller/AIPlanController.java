package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.AIPlanRequest;
import com.drikek.improveMe.dto.AIPlanResponse;
import com.drikek.improveMe.service.AIPlanService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai-plans")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AIPlanController {

    private final AIPlanService aiPlanService;

    @PostMapping
    public ResponseEntity<@NonNull AIPlanResponse> createAIPlan(@AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestBody AIPlanRequest request) {

        AIPlanResponse response = aiPlanService.createAIPlan(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/user")
    public ResponseEntity<@NonNull List<AIPlanResponse>> getAIPlanForUser(@AuthenticationPrincipal UserDetails userDetails) {

        List<AIPlanResponse> response = aiPlanService.getAIPlansForUser(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull AIPlanResponse> getAIPlanById(@PathVariable Long id) {

        AIPlanResponse response = aiPlanService.getAIPlanById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/latest")
    public ResponseEntity<@NonNull AIPlanResponse> getLatestAIPlan(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @RequestParam Long categoryId) {

        AIPlanResponse response = aiPlanService.getLatestAIPlan(userDetails.getUsername(), categoryId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAIPlan(@PathVariable Long id) {

        aiPlanService.deleteAIPlan(id);

        return ResponseEntity.ok("AI Plan deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull AIPlanResponse> updateAIPlan(@PathVariable Long id,
                                                                @RequestBody AIPlanRequest request) {

        AIPlanResponse response = aiPlanService.updateAIPlan(id, request);

        return ResponseEntity.ok(response);
    }

}
