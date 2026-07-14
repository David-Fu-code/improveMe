package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Habit;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unused")
public interface HabitRepository extends JpaRepository<@NonNull Habit, @NonNull Long> {

    // All habits of a specific user
    List<Habit> findByUserId(Long userId);

    // All habits of a specific user inside a specific category
    List<Habit> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
