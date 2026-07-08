package com.drikek.improveMe.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonPropertyOrder({ "id", "userId", "categoryId", "assessmentId", "categoryName", "title", "description", "status", "startDate", "endDate", "createdAt", "completedAt" })
public class AIPlanResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long assessmentId;
    private String categoryName;
    private String title;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
