package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssessmentRequest {
    private Long categoryId;
    private String questions;
    private String answers;
    private Integer score;
    private String level;
}
