package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequest {
    // Prevent user to send different fields
    private String name;
    private String description;
}
