package com.drikek.improveMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "habit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Habit {

    @Id
    @SequenceGenerator(sequenceName = "habit_sequence", name = "habit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // for registered users

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // belongs to a specific category

    @Column(name = "guest_token")
    private String guestToken; // Can use app without registered

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private boolean isSuggested;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
