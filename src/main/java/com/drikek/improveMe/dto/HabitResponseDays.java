package com.drikek.improveMe.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonPropertyOrder({"id", "title", "description", "category", "timestamp"})
public class HabitResponseDays {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime timestamp;

}
