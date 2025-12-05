package com.drikek.improveMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "habit_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
// This represents one day of progress for one habit.
public class HabitRecord {

    @Id
    @SequenceGenerator(sequenceName = "habit_record_sequence", name = "habit_record_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_record_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(nullable = false)
    private int dayIndex;

    private boolean checked;

    private LocalDateTime checkedAt;

    public HabitRecord(Habit habit, int dayIndex) {
        this.habit = habit;
        this.dayIndex = dayIndex;
        this.checked = false;
        this.checkedAt = null;
    }
}
