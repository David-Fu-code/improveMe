package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogoutAllResponse {
    private String message;
    private LocalDateTime timestamp;
}
