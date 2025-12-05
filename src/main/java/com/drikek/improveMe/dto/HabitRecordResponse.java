package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HabitRecordResponse {
    private int dayIndex; // 0 = day 1
    private boolean checked; // true/false
    private LocalDateTime checkedAt;
}
