package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.*;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.Habit;
import com.drikek.improveMe.entity.HabitRecord;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.exception.BadRequestException;
import com.drikek.improveMe.repository.CategoryRepository;
import com.drikek.improveMe.repository.HabitRecordRepository;
import com.drikek.improveMe.repository.HabitRepository;
import com.drikek.improveMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HabitRecordService habitRecordService;

    // Create habit
    public HabitResponse createHabit(Long userId, HabitRequest request) {

        // Validations
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found", 404));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AuthException("Category not found", 404));

        // Create habit
        Habit habit = new Habit();
        habit.setUser(user);
        habit.setCategory(category);
        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setSuggested(false);
        habit.setGuestToken(null);
        habit.setStartDate(LocalDateTime.now());
        habit.setCreatedAt(LocalDateTime.now());

        // Save Habit
        Habit saveHabit = habitRepository.save(habit);

        habitRecordService.initializeWeek(saveHabit);

        return toHabitResponse(saveHabit);
    }

    // Created guest habit
    public HabitResponse createHabitForGuest(String guestToken, HabitRequest request) {

        // Validation
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AuthException("Category not found", 404));

        // Create habit for guest
        Habit habit = new Habit();
        habit.setGuestToken(guestToken);
        habit.setCategory(category);
        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setSuggested(false);
        habit.setStartDate(LocalDateTime.now());
        habit.setCreatedAt(LocalDateTime.now());

        Habit saveHabit = habitRepository.save(habit);

        // Create 7 check-ins
        habitRecordService.initializeWeek(saveHabit);

        return toHabitResponse(saveHabit);
    }

    // Obtain user habits
    public List<HabitResponse> getHabitsForUser(Long userId) {

        // Validation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found", 404));

        return habitRepository.findByUserId(userId).stream()// convert list -> Stream to apply transformations
                .map(this::toHabitResponse) // converts each Habit entity into a HabitResponse DTO using a helper method toHabitResponse
                .toList(); // Collects result back into list

    }

    // Obtain guest habits
    public List<HabitResponse> getHabitsForGuest(String guestToken) {

        // No need Validation (guest)
        return habitRepository.findByGuestToken(guestToken).stream()
                .map(this::toHabitResponse)
                .toList();

    }

    // Obtain habit with 7 day check-ins
    public HabitWithRecordResponse getHabitsWithChecks(Long habitId) {

        // Validation
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new AuthException("Habit not found", 404));

        List<HabitRecordResponse> records = habitRecordService.getWeekRecord(habit.getId()).stream()
                .map(r -> new HabitRecordResponse(r.getDayIndex(), r.isChecked(), r.getCheckedAt()))
                .toList();

        return HabitWithRecordResponse.builder()
                .habit(toHabitResponseDays(habit))
                .record(records)
                .build();
    }

    // Check/Uncheck habit
    public HabitRecordResponse toggleCheck(Long habitId, int dayIndex) {

        // Validation
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new AuthException("Habit not found", 404));

        HabitRecord record = habitRecordService.getRecord(habitId, dayIndex)
                .orElseThrow(() -> new AuthException("HabitRecord not found", 404));

        if (!record.getHabit().getId().equals(habitId)) {
            throw new BadRequestException("Record does not belong to this habit", 400);
        }

        HabitRecord update;

        if (record.isChecked()) {
            update = habitRecordService.unmarkCompleted(habitId, dayIndex);
        } else {
            update = habitRecordService.markCompleted(habitId, dayIndex);
        }

        return new HabitRecordResponse(
                update.getDayIndex(),
                update.isChecked(),
                update.getCheckedAt()
        );
    }

    // Update habit
    public HabitResponse updateHabit(Long habitId, HabitRequest request) {

        // Validation
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new AuthException("Habit not found", 404));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AuthException("Category not found", 404));

        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setCategory(category);

        Habit update = habitRepository.save(habit);

        return toHabitResponse(update);
    }

    // Eliminate habit
    @Transactional
    public void deleteHabit(long habitId) {

        // Validation
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new AuthException("Habit not found", 404));

        // Eliminate 7 days register
        habitRecordService.deleteRecordByHabitId(habitId);

        // Eliminate habit
        habitRepository.deleteById(habitId);

    }

    // Habit response
    private HabitResponse toHabitResponse(Habit habit) {
        return HabitResponse.builder()
                .id(habit.getId())
                .title(habit.getTitle())
                .description(habit.getDescription())
                .categoryId(habit.getCategory().getId())
                .categoryName(habit.getCategory().getName())
                .startDate(habit.getStartDate())
                .createdAt(habit.getCreatedAt())
                .build();
    }

    // Habit response 7 day check
    private HabitResponseDays toHabitResponseDays(Habit habit) {
        return HabitResponseDays.builder()
                .id(habit.getId())
                .title(habit.getTitle())
                .description(habit.getDescription())
                .category(habit.getCategory().getName())
                .timestamp(habit.getCreatedAt())
                .build();
    }

    // TODO methods to "Load habits filtered by category" "Returns only habits relevant for today"

}
