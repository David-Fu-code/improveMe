package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Habit;
import com.drikek.improveMe.entity.HabitRecord;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("unused")
public interface HabitRecordRepository  extends JpaRepository<@NonNull HabitRecord, @NonNull Long> {

    // Get all 7 days ordered correctly for ONE habit
    List<HabitRecord> findByHabitOrderByDayIndex(Habit habit);

    // Get all 7 days ordered correctly for ONE habit
    List<HabitRecord> findByHabitIdOrderByDayIndexAsc(Long habitId);

    // Get ONE specific day for a habit (for check/uncheck)
    Optional<HabitRecord> findByHabitIdAndDayIndex(Long habitId, int dayIndex);

    // Record of a habit on a specific day
    Optional<HabitRecord> findByHabitAndCheckedAtBetween(Habit habit, LocalDateTime start, LocalDateTime end);

    // Records for a user's habits on a specific day
    List<HabitRecord> findByHabitUserIdAndCheckedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // Check if a habit already has records (to avoid duplicates)
    boolean existsByHabitId(Long habitId);

    // Delete habit by habitId
    void deleteByHabitId(Long habitId);
}

