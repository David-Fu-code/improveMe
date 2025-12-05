package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitRequest {
    private String title;
    private String description;
    private Long categoryId;
    private Long userId;
}
