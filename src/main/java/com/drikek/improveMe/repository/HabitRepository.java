package com.drikek.improveMe.repository;

import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.Habit;
import com.drikek.improveMe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository  extends JpaRepository<Habit, Long> {

    // All habits of a specific user
    List<Habit> findByUserId (Long userId);

    // All habits of a specific user inside a specific category
    List<Habit> findByUserIdAndCategoryId(Long userId, Long categoryId);

    // All habits for guest users
    List<Habit> findByGuestToken(String guestToken);
}
