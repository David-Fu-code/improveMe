package com.drikek.improveMe.service;

import com.drikek.improveMe.entity.Habit;
import com.drikek.improveMe.entity.HabitRecord;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.HabitRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class HabitRecordService {

    private final HabitRecordRepository habitRecordRepository;

    // Create the 7-day week when habit is created
    public void initializeWeek(Habit habit) {

        // Validation prevent duplicated weeks
        if (habitRecordRepository.existsByHabitId(habit.getId())) {
            return;
        }

        List<HabitRecord> records = new ArrayList<>();

        for (int day = 0; day < 7; day++ ) {
            HabitRecord record = new HabitRecord(habit, day);
            records.add(record);
        }
        habitRecordRepository.saveAll(records);
    }

    // Get all 7 days ordered
    public List<HabitRecord> getWeekRecord(Long habitId) {
        return habitRecordRepository.findByHabitIdOrderByDayIndexAsc(habitId);
    }

    // Get one specific day
    public Optional<HabitRecord> getRecord(Long habitId, int dayIndex){
        return habitRecordRepository.findByHabitIdAndDayIndex(habitId, dayIndex);
    }

    // Mark a day is completed
    public HabitRecord markCompleted(Long habitId, int dayIndex){

        // Validation
        HabitRecord record = habitRecordRepository.findByHabitIdAndDayIndex(habitId, dayIndex)
                .orElseThrow(() -> new AuthException("Habit record not found", 404));

        record.setChecked(true);
        record.setCheckedAt(LocalDateTime.now());

        return habitRecordRepository.save(record);
    }

    // Unmark a day
    public HabitRecord unmarkCompleted(Long habitId, int dayIndex){

        // Validation
        HabitRecord record = habitRecordRepository.findByHabitIdAndDayIndex(habitId, dayIndex)
                .orElseThrow(() -> new AuthException("Habit record not found", 404));

        record.setChecked(false);
        record.setCheckedAt(null);

        return habitRecordRepository.save(record);
    }

    // Deleted all records when habit is deleted
    public void deleteRecordByHabitId(Long habitId) {

        List<HabitRecord> records = habitRecordRepository
                .findByHabitIdOrderByDayIndexAsc(habitId);

        habitRecordRepository.deleteAll(records);
    }

    // Check if week already exists
    public boolean existsWeek(Long habitId) {
        return habitRecordRepository.existsByHabitId(habitId);
    }

}
