package com.drikek.improveMe.dto;

import lombok.Data;

@Data
public class AIPlanGenerationRequest {
    private Long assessmentId;
    private Long categoryId;
    private String userGoals;
    private String preferences;
}
