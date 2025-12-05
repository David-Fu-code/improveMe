package com.drikek.improveMe.dto;

import com.drikek.improveMe.entity.HabitRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HabitWithRecordResponse {
    // Returns habit info and 7 check-ins
    private HabitResponseDays habit;
    private List<HabitRecordResponse> record;
}
