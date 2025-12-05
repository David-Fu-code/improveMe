package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.HabitRecordResponse;
import com.drikek.improveMe.dto.HabitRequest;
import com.drikek.improveMe.dto.HabitResponse;
import com.drikek.improveMe.dto.HabitWithRecordResponse;
import com.drikek.improveMe.entity.Habit;
import com.drikek.improveMe.exception.BadRequestException;
import com.drikek.improveMe.service.HabitService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    // CREATE habit
    @PostMapping
    public ResponseEntity<@NonNull HabitResponse> createHabit(@RequestBody HabitRequest request) {
        HabitResponse response;

        if (request.getUserId() != null) {
            response = habitService.createHabit(request.getUserId(), request);
        } else {
            throw new BadRequestException("userId must be provided", 400);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // CREATE habit for guest
    @PostMapping("/guest/{guestToken}")
    public ResponseEntity<@NonNull HabitResponse> createHabitForGuest(@PathVariable String guestToken, @RequestBody HabitRequest request) {

        HabitResponse response = habitService.createHabitForGuest(guestToken, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET habits for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<@NonNull List<HabitResponse>> getHabitsForUser(@PathVariable Long userId) {

        List<HabitResponse> response = habitService.getHabitsForUser(userId);
        return ResponseEntity.ok(response);
    }

    // GET habits for guest
    @GetMapping("/guest/{guestToken}")
    public ResponseEntity<@NonNull List<HabitResponse>> getHabitsForGuest(@PathVariable String guestToken) {

        List<HabitResponse> response = habitService.getHabitsForGuest(guestToken);
        return ResponseEntity.ok(response);
    }

    // GET single habit with it's 7-day-records
    @GetMapping("/{habitId}")
    public ResponseEntity<@NonNull HabitWithRecordResponse> getHabitRecord(@PathVariable Long habitId){

        HabitWithRecordResponse response = habitService.getHabitsWithChecks(habitId);
        return ResponseEntity.ok(response);
    }

    // UPDATE habit
    @PutMapping("/{habitId}")
    public ResponseEntity<@NonNull HabitResponse> updateHabit(@PathVariable Long habitId, @RequestBody HabitRequest request) {

        HabitResponse response = habitService.updateHabit(habitId, request);
        return ResponseEntity.ok(response);
    }

    // DELETE habit
    @DeleteMapping("/{habitId}")
    public ResponseEntity<?> deleteHabit(@PathVariable Long habitId) {

        habitService.deleteHabit(habitId);
        return ResponseEntity.ok("Habit deleted successfully");
    }

    // TOGGLE a check
    @PatchMapping("/{habitId}/check/{dayIndex}")
    public ResponseEntity<@NonNull HabitRecordResponse> checkHabit(@PathVariable Long habitId, @PathVariable int dayIndex) {

        HabitRecordResponse response = habitService.toggleCheck(habitId, dayIndex);
        return ResponseEntity.ok(response);
    }



}
