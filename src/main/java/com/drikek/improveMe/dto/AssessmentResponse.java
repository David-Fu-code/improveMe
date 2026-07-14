package com.drikek.improveMe.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"id", "userId", "categoryId", "categoryName", "questions", "answers", "score", "level", "createdAt"})
public class AssessmentResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String questions;
    private String answers;
    private Integer score;
    private String level;
    private LocalDateTime createdAt;
}
