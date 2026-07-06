package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AIPlanRequest {
    private Long categoryId;
    private Long assessmentId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
