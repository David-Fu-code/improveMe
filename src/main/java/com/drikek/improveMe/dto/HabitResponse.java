package com.drikek.improveMe.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonPropertyOrder({ "id", "title", "description", "categoryId", "categoryName", "startDate", "createdAt" })
public class HabitResponse {
    private Long id;
    private String title;
    private String description;

    private Long categoryId;
    private String categoryName;

    private LocalDateTime startDate;
    private LocalDateTime createdAt;
}
